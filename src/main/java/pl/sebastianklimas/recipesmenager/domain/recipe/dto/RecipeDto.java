package pl.sebastianklimas.recipesmenager.domain.recipe.dto;

import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;

import java.util.HashSet;
import java.util.Set;

public class RecipeDto {
    private long id;
    private String name;
    private String manual;
    private Set<IngredientDto> ingredients = new HashSet<>();

    public RecipeDto() {
    }

    public RecipeDto(long id, String name, String manual, Set<IngredientDto> ingredients) {
        this.id = id;
        this.name = name;
        this.manual = manual;
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public Set<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }
}
