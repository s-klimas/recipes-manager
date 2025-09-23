package pl.sebastianklimas.recipesmenager.users;

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
import pl.sebastianklimas.recipesmenager.users.exceptions.UserNotFoundException;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponseDto> registerUser(
            @Valid @RequestBody RegisterUserRequestDto requestDto,
            UriComponentsBuilder uriBuilder) {
        RegisterUserResponseDto userDto = userService.registerUser(requestDto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
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
}
