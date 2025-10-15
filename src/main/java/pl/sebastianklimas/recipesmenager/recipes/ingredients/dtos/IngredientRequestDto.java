package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientRequestDto {
    @NotBlank
    @Schema(name = "name", example = "Milk", description = "Ingredient name")
    private String name;
    @Schema(name = "count", example = "0.5", description = "Count of ingredients")
    private BigDecimal count;
    @NotBlank
    @Schema(name = "unit", example = "l", description = "Ingredient unit")
    private String unit;
}
