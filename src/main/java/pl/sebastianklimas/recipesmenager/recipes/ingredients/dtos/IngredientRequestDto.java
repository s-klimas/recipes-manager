package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientRequestDto {
    @NotBlank
    @Schema(name = "Ingredient name", example = "Milk")
    private String name;
    @Schema(name = "Count of ingredients", example = "0.5")
    private BigDecimal count;
    @NotBlank
    @Schema(name = "Ingredient unit", example = "l")
    private String unit;
}
