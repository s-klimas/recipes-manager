package pl.sebastianklimas.recipesmenager.recipes;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RecipeRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Manual is required")
    private String manual;
    private Set<IngredientRequest> ingredients = new HashSet<>();
}
