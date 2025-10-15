package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientResponseDto {
    @Schema(name = "id", example = "1", description = "Ingredient ID")
    private long id;
    @Schema(name = "name", example = "Milk", description = "Ingredient name")
    private String name;
    @Schema(name = "count", example = "0.5", description = "Count of ingredients")
    private BigDecimal count;
    @Schema(name = "unit", example = "l", description = "Ingredient unit")
    private String unit;

    public IngredientResponseDto() {
    }

    @Override
    public String toString() {
        return id + ". Ingredient: " + name + " " + count.toString() + " " + unit;
    }


}
