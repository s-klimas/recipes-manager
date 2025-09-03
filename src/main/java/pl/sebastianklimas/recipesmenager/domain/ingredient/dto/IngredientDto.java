package pl.sebastianklimas.recipesmenager.domain.ingredient.dto;

import lombok.Data;

@Data
public class IngredientDto {
    private long id;
    private String name;
    private int count;
    private String unit;

    public IngredientDto() {
    }

    public IngredientDto(long id, String name, int count, String unit) {
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
