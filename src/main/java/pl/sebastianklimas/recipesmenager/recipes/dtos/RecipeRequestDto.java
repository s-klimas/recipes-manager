package pl.sebastianklimas.recipesmenager.recipes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientRequest;

import java.util.HashSet;
import java.util.Set;

@Data
public class RecipeRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Manual is required")
    private String manual;
    private Set<IngredientRequest> ingredients = new HashSet<>();
}
