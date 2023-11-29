package pl.sebastianklimas.recipesmenager.domain.ingredient;

import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;

public class IngredientDtoMapper {
    public static IngredientDto map(Ingredient ingredient) {
        long id = ingredient.getId();
        String name = ingredient.getName();
        int count = ingredient.getCount();
        String unit = ingredient.getUnit();
        return new IngredientDto(id, name, count, unit);
    }
}
