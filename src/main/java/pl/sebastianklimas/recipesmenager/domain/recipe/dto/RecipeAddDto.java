package pl.sebastianklimas.recipesmenager.domain.recipe.dto;

public class RecipeAddDto {
    private String name;
    private String manual;

    public RecipeAddDto() {
    }

    public RecipeAddDto(String name, String manual) {
        this.name = name;
        this.manual = manual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }
}
