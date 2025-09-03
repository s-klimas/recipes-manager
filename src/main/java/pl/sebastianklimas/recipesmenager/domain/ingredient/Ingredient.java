package pl.sebastianklimas.recipesmenager.domain.ingredient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.Recipe;

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

    public Ingredient() {
    }

    public Ingredient(Recipe recipe, String name, int count, String unit) {
        this.recipe = recipe;
        this.name = name;
        this.count = count;
        this.unit = unit;
    }

}
