package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientResponseDto {
    @Schema(name = "Ingredient ID", example = "1")
    private long id;
    @Schema(name = "Ingredient name", example = "Milk")
    private String name;
    @Schema(name = "Count of ingredients", example = "0.5")
    private BigDecimal count;
    @Schema(name = "Ingredient unit", example = "l")
    private String unit;

    public IngredientResponseDto() {
    }

    @Override
    public String toString() {
        return id + ". Ingredient: " + name + " " + count.toString() + " " + unit;
    }


}
