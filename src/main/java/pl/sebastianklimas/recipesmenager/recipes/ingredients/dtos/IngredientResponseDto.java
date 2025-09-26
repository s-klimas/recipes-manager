package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientResponseDto {
    private long id;
    private String name;
    private BigDecimal count;
    private String unit;

    public IngredientResponseDto() {
    }

    @Override
    public String toString() {
        return id + ". Ingredient: " + name + " " + count.toString() + " " + unit;
    }


}
