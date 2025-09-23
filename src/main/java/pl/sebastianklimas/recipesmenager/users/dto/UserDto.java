package pl.sebastianklimas.recipesmenager.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String login;
    private String email;
    private String status;
    private List<String> roles;
}
