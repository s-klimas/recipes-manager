package pl.sebastianklimas.recipesmenager.recipes.exceptions;

public class RecipeNotAvailableForCurrentUserException extends RuntimeException {
    public RecipeNotAvailableForCurrentUserException(String message) {
        super(message);
    }
}
