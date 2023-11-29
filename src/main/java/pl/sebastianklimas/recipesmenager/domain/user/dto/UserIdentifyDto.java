package pl.sebastianklimas.recipesmenager.domain.user.dto;

public class UserIdentifyDto {
    private final Long id;
    private final String email;

    public UserIdentifyDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
