package pl.sebastianklimas.recipesmenager.domain.ingredient;

import pl.sebastianklimas.recipesmenager.recipes.ingredients.Ingredient;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientResponseDto;

public class IngredientDtoMapper {
    public static IngredientResponseDto map(Ingredient ingredient) {
        long id = ingredient.getId();
        String name = ingredient.getName();
        int count = ingredient.getCount();
        String unit = ingredient.getUnit();
        return new IngredientResponseDto(id, name, count, unit);
    }
}
