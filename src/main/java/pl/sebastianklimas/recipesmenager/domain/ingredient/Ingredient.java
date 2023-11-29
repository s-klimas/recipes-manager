package pl.sebastianklimas.recipesmenager.domain.ingredient;

import jakarta.persistence.*;
import pl.sebastianklimas.recipesmenager.domain.recipe.Recipe;

@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private String name;
    private int count;
    private String unit;

    public Ingredient() {
    }

    public Ingredient(Recipe recipe, String name, int count, String unit) {
        this.recipe = recipe;
        this.name = name;
        this.count = count;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
