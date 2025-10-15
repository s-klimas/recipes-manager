package pl.sebastianklimas.recipesmenager.recipes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientResponseDto;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class RecipeResponseDto {
    @NotNull
    @Schema(name = "id", example = "1", description = "Recipe ID")
    private Long id;
    @NotBlank(message = "Name is required")
    @Schema(name = "name", example = "Mom's recipe", description = "Recipe name")
    private String name;
    @NotBlank(message = "Instructions are required")
    @Schema(name = "instructions", example = "Start with mixing all ingredients together...", description = "Instructions on how to make the recipe")
    private String instructions;
    private Set<IngredientResponseDto> ingredients = new HashSet<>();
}
