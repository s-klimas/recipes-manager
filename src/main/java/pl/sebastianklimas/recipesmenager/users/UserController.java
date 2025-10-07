package pl.sebastianklimas.recipesmenager.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.sebastianklimas.recipesmenager.users.dto.ChangePasswordRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserRequestDto;
import pl.sebastianklimas.recipesmenager.users.dto.RegisterUserResponseDto;
import pl.sebastianklimas.recipesmenager.users.dto.UserDto;
import pl.sebastianklimas.recipesmenager.users.exceptions.DuplicateUserException;
import pl.sebastianklimas.recipesmenager.users.exceptions.RoleNotFoundException;
import pl.sebastianklimas.recipesmenager.users.exceptions.UserNotFoundException;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Registers a user.")
    public ResponseEntity<RegisterUserResponseDto> registerUser(
            @Parameter(description = "The new users data.")
            @Valid @RequestBody RegisterUserRequestDto requestDto,
            UriComponentsBuilder uriBuilder) {
        RegisterUserResponseDto userDto = userService.registerUser(requestDto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get the user by ID.")
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "The ID of the user.")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes the users.")
    public void deleteUser(
            @Parameter(description = "The ID of the user.")
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Change password for user.")
    public void changePassword(
            @Parameter(description = "The ID of the user.")
            @PathVariable Long id,
            @Parameter(description = "The old and new password.")
            @RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(id, request);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUser(DuplicateUserException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Void> handleRoleNotFound() {
        return ResponseEntity.notFound().build();
    }
}
