package pl.sebastianklimas.recipesmenager.recipes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.auth.AuthService;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeRequestDto;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeResponseDto;
import pl.sebastianklimas.recipesmenager.recipes.exceptions.RecipeNotAvailableForCurrentUserException;
import pl.sebastianklimas.recipesmenager.recipes.exceptions.RecipeNotFoundException;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.Ingredient;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.IngredientMapper;
import pl.sebastianklimas.recipesmenager.users.User;

import java.util.List;

@AllArgsConstructor
@Service
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;
    private final AuthService authService;
    private final IngredientMapper ingredientMapper;

    public RecipeResponseDto createRecipe(RecipeRequestDto recipeDto) {
        User currentUser = authService.getCurrentUser();

        Recipe recipe = recipeMapper.toEntity(recipeDto);
        recipe.setVisibility(Visibility.PRIVATE.toString());
        recipe.setUser(currentUser);

        recipe.getIngredients().forEach(ingredient -> {
            ingredient.setRecipe(recipe);
        });

        recipeRepository.save(recipe);

        return recipeMapper.toDto(recipe);
    }

    public List<RecipeResponseDto> getAllRecipes() {
        User currentUser = authService.getCurrentUser();
        return recipeRepository.findAll().stream()
                .filter(recipe -> (currentUser.equals(recipe.getUser()) || recipe.getVisibility().equals(Visibility.PUBLIC.toString())))
                .map(recipeMapper::toDto)
                .toList();
    }

    public RecipeResponseDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        if (authService.getCurrentUser().equals(recipe.getUser()) || recipe.getVisibility().equals(Visibility.PUBLIC.toString())) {
            return recipeMapper.toDto(recipe);
        }

        throw new RecipeNotAvailableForCurrentUserException("Recipe does not belong to current user and is not public");
    }

    public RecipeResponseDto updateRecipe(Long id, RecipeRequestDto recipeDto) {
        Recipe recipe =  recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        if (!recipe.getUser().equals(authService.getCurrentUser())) throw new RecipeNotAvailableForCurrentUserException("Recipe does not belong to current user");

        recipe.getIngredients().clear();

        recipeMapper.update(recipeDto, recipe);

        recipe.getIngredients().forEach(ingredient -> {
            ingredient.setRecipe(recipe);
        });

        recipeRepository.save(recipe);

        return recipeMapper.toDto(recipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        if (!recipe.getUser().equals(authService.getCurrentUser())) throw new RecipeNotAvailableForCurrentUserException("Recipe does not belong to current user");
        recipeRepository.delete(recipe);
    }
}
