package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IngredientRequest {
    @NotBlank
    private String name;
    private int count;
    @NotBlank
    private String unit;
}
