package pl.sebastianklimas.recipesmenager.recipes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.Ingredient;
import pl.sebastianklimas.recipesmenager.users.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "instructions", columnDefinition = "text")
    private String instructions;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "recipe")
    @OneToMany(mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @PrePersist
    public void onPrePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}


