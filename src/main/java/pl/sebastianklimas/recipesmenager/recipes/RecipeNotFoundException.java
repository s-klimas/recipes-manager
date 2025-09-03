package pl.sebastianklimas.recipesmenager.recipes;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
