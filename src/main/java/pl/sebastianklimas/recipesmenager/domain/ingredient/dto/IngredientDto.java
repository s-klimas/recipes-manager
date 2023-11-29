package pl.sebastianklimas.recipesmenager.domain.ingredient.dto;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return id + ". Ingredient: " + name + " " + count + " " + unit;
    }
}
