package pl.sebastianklimas.recipesmenager.ai.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/ai")
@AllArgsConstructor
@Tag(name = "AI Logic")
public class ChatController {
    private final ChatService chatService;


    @GetMapping("/chat")
    @Operation(summary = "Sends user message to LLM (only for logged in users - allows AI tools to operate on database).")
    public String chat(
            @Parameter(description = "Users message.")
            @RequestBody String message
    ) {
        return chatService.chat(message);
    }

    @GetMapping("/assist")
    @Operation(summary = "Sends users message to LLM (for non logged users).")
    public String assist(
            @Parameter(description = "Users message.")
            @RequestBody String message
    ) {
        return chatService.assist(message);
    }

    @PostMapping("/image-to-recipe")
    @Operation(summary = "Allows to send an image of the recipe with a message to LLM (only for logged in users - allows AI tools to operate on database).")
    public ResponseEntity<String> imageToRecipe(
            @Parameter(description = "Image od the recipe.")
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Users message.")
            @RequestParam("message") String message
    ) {
        try (InputStream inputStream = file.getInputStream()) {
            var response = chatService.imageToRecipe(inputStream, file.getContentType(), message);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }
}
