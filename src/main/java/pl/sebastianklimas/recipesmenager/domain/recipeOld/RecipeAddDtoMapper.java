package pl.sebastianklimas.recipesmenager.domain.recipeOld;

import pl.sebastianklimas.recipesmenager.domain.recipeOld.dto.RecipeAddDto;

public class RecipeAddDtoMapper {
    static RecipeAddDto map(Recipe recipe) {
        String name = recipe.getName();
        String manual = recipe.getManual();
        return new RecipeAddDto(name, manual);
    }
}
