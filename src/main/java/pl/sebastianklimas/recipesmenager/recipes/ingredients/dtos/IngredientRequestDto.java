package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientRequestDto {
    @NotBlank
    private String name;
    private BigDecimal count;
    @NotBlank
    private String unit;
}
