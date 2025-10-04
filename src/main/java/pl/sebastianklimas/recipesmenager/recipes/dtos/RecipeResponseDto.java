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
    @Schema(name = "Recipe ID", example = "1")
    private Long id;
    @NotBlank(message = "Name is required")
    @Schema(name = "Recipe name", example = "Mom's recipe")
    private String name;
    @NotBlank(message = "Instructions are required")
    @Schema(name = "Instructions on how to make the recipe", example = "Start with mixing all ingredients together...")
    private String instructions;
    private Set<IngredientResponseDto> ingredients = new HashSet<>();
}
