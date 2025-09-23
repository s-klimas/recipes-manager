package pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos;

import lombok.Data;

@Data
public class IngredientResponseDto {
    private long id;
    private String name;
    private int count;
    private String unit;

    public IngredientResponseDto() {
    }

    public IngredientResponseDto(long id, String name, int count, String unit) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return id + ". Ingredient: " + name + " " + count + " " + unit;
    }


}
