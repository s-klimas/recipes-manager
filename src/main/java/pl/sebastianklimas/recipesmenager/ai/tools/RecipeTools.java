package pl.sebastianklimas.recipesmenager.ai.tools;

import lombok.AllArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import pl.sebastianklimas.recipesmenager.recipes.RecipeService;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeRequestDto;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeResponseDto;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.dtos.IngredientRequestDto;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class RecipeTools {
    private final RecipeService recipeService;

    @Tool(description = "Creates a new recipe with name, instructions how to make it and set of ingredients which have name, count and unit.")
    public RecipeResponseDto createRecipe(String name, String instructions, Set<IngredientRequestDto> ingredients) {
        return recipeService.createRecipe(new RecipeRequestDto(name, instructions, ingredients));
    }

    @Tool(description = "Find recipe in database with given id.")
    public RecipeResponseDto getRecipe(Long id) {
        return recipeService.getRecipeById(id);
    }

    @Tool(description = "Find all available recipes in database for logged user.")
    public List<RecipeResponseDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @Tool(description = "Updates recipe with given id. Updates name, instructions and ingredients which have name, count and unit.")
    public RecipeResponseDto updateRecipe(Long id, String name, String instructions, Set<IngredientRequestDto> ingredients) {
        return recipeService.updateRecipe(id, new RecipeRequestDto(name, instructions, ingredients));
    }
}
