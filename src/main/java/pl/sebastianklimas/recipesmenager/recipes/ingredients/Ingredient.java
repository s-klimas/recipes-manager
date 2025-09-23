package pl.sebastianklimas.recipesmenager.recipes.ingredients;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sebastianklimas.recipesmenager.recipes.Recipe;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column(name = "name")
    private String name;

    @Column(name = "count")
    private int count;

    @Column(name = "unit")
    private String unit;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Ingredient() {
    }

    public Ingredient(Recipe recipe, String name, int count, String unit) {
        this.recipe = recipe;
        this.name = name;
        this.count = count;
        this.unit = unit;
    }

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

    @Override
    public String toString() {
        return name + " " + count + " " + unit;
    }
}
