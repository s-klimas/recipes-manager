package pl.sebastianklimas.recipesmenager.domain.ingredient.dto;

import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientResponseDto;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListWrapperDto {
    private List<IngredientResponseDto> ingredientDtoList = new ArrayList<>();

    public IngredientsListWrapperDto() {
    }

    public IngredientsListWrapperDto(List<IngredientResponseDto> ingredientDtoList) {
        this.ingredientDtoList = ingredientDtoList;
    }

    public void addIngredientToList(IngredientResponseDto ingredientDto) {
        ingredientDtoList.add(ingredientDto);
    }

    public List<IngredientResponseDto> getIngredientDtoList() {
        return ingredientDtoList;
    }

    public void setIngredientDtoList(List<IngredientResponseDto> ingredientDtoList) {
        this.ingredientDtoList = ingredientDtoList;
    }
}
