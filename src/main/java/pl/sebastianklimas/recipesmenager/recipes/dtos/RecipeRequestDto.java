package pl.sebastianklimas.recipesmenager.recipes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientRequestDto;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeRequestDto {
    @NotBlank(message = "Name is required")
    @Schema(name = "name", example = "Mom's recipe", description = "Recipe name")
    private String name;
    @NotBlank(message = "Instructions are required")
    @Schema(name = "instructions", example = "Start with mixing all ingredients together...", description = "Instructions on how to make the recipe")
    private String instructions;
    private Set<IngredientRequestDto> ingredients;
}
