package pl.sebastianklimas.recipesmenager.recipes.ingredients;

import org.mapstruct.Mapper;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientRequestDto;

@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredient toEntity(IngredientRequestDto ingredientRequest);
}
