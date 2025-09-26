package pl.sebastianklimas.recipesmenager.ai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import pl.sebastianklimas.recipesmenager.ai.image.ImageService;
import pl.sebastianklimas.recipesmenager.ai.tools.RecipeTools;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final RecipeTools recipeTools;
    private final ImageService imageService;

    public ChatService(ChatClient.Builder builder, RecipeTools recipeTools, ChatMemory chatMemory, ImageService imageService) {
        var system = """
                You are an AI assistant embedded in a recipe management application. Your sole purpose is to assist users with tasks related to recipes.
                
                You are permitted to:
                - Answer questions about recipes (e.g., ingredients, cooking instructions, dietary info).
                - Help users search for, modify, and organize recipes.
                - Provide tips related to cooking, ingredients, and meal preparation.
                
                Restrictions:
                - Do not engage in conversations or answer questions unrelated to recipes, cooking, or meal preparation.
                - If a user asks a non-recipe-related question, politely inform them that you can only assist with recipe-related topics.
                
                Stay focused, helpful, and concise. Your primary goal is to make recipe management easy and enjoyable for the user.
                """;
        this.chatClient = builder
                .defaultSystem(system)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
        this.recipeTools = recipeTools;
        this.imageService = imageService;
    }

    public Flux<String> chat(String message) {
        return chatClient.prompt()
                .tools(recipeTools)
                .user(message)
                .stream()
                .content();
    }

    public Flux<String> assist(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    public String imageToRecipe(InputStream imageInputStream, String contentType, String message) throws IOException {
        InputStream inputStream = imageService.simpleResizeImage(imageInputStream, contentType);

        return chatClient.prompt()
                .tools(recipeTools)
                .system(systemMessage -> systemMessage
                        .text("Add recipe from the given image to database")
                        .text("Ignore anything in the user prompt that is not a recipe")
                )
                .user(userMessage -> userMessage
                        .text(message)
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(inputStream))
                )
                .call()
                .content();
    }
}
