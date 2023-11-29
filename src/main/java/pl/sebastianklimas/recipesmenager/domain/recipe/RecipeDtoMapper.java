package pl.sebastianklimas.recipesmenager.domain.recipe;

import pl.sebastianklimas.recipesmenager.domain.ingredient.IngredientDtoMapper;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;
import pl.sebastianklimas.recipesmenager.domain.recipe.dto.RecipeDto;

import java.util.Set;
import java.util.stream.Collectors;

public class RecipeDtoMapper {
    static RecipeDto map(Recipe recipe) {
        long id = recipe.getId();
        String name = recipe.getName();
        String manual = recipe.getManual();
        Set<IngredientDto> ingredients = recipe.getIngredients().stream().map(IngredientDtoMapper::map).collect(Collectors.toSet());
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(id);
        recipeDto.setName(name);
        recipeDto.setManual(manual);
        recipeDto.setIngredients(ingredients);
        return recipeDto;
    }
}
