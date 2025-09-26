package pl.sebastianklimas.recipesmenager.recipes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientRequestDto;

import java.util.Set;

@AllArgsConstructor
@Data
public class RecipeRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Instructions are required")
    private String instructions;
    private Set<IngredientRequestDto> ingredients;
}
