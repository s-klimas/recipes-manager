package pl.sebastianklimas.recipesmenager.ai.chat;

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
public class ChatController {
    private final ChatService chatService;


    @GetMapping("/chat")
    public Flux<String> chat(@RequestBody String message) {
        return chatService.chat(message);
    }

    @GetMapping("/assist")
    public Flux<String> assist(@RequestBody String message) {
        return chatService.assist(message);
    }

    @PostMapping("/image-to-recipe")
    public ResponseEntity<String> imageToRecipe(@RequestParam("file") MultipartFile file, @RequestParam("message") String message) {
        try (InputStream inputStream = file.getInputStream()) {
            var response = chatService.imageToRecipe(inputStream, file.getContentType(), message);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }
}
