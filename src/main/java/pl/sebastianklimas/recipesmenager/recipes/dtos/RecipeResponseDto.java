package pl.sebastianklimas.recipesmenager.recipes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class RecipeResponseDto {
    @NotNull
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Instructions are required")
    private String instructions;
    private Set<IngredientDto> ingredients = new HashSet<>();
}
