package pl.sebastianklimas.recipesmenager.domain.ingredient.dto;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListWrapperDto {
    private List<IngredientDto> ingredientDtoList = new ArrayList<>();

    public IngredientsListWrapperDto() {
    }

    public IngredientsListWrapperDto(List<IngredientDto> ingredientDtoList) {
        this.ingredientDtoList = ingredientDtoList;
    }

    public void addIngredientToList(IngredientDto ingredientDto) {
        ingredientDtoList.add(ingredientDto);
    }

    public List<IngredientDto> getIngredientDtoList() {
        return ingredientDtoList;
    }

    public void setIngredientDtoList(List<IngredientDto> ingredientDtoList) {
        this.ingredientDtoList = ingredientDtoList;
    }
}
