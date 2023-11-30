package pl.sebastianklimas.recipesmenager.domain.recipe;

import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.domain.ingredient.Ingredient;
import pl.sebastianklimas.recipesmenager.domain.ingredient.IngredientRepository;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;
import pl.sebastianklimas.recipesmenager.domain.recipe.dto.RecipeDto;
import pl.sebastianklimas.recipesmenager.domain.user.User;
import pl.sebastianklimas.recipesmenager.domain.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<RecipeDto> findAllRecipesByUserId(Long userId) {
        return recipeRepository.findAllByUserId(userId).stream().map(RecipeDtoMapper::map).collect(Collectors.toList());
    }

    @Transactional
    public void addRecipeWithIngredients(String currentUserEmail, String recipeName, String recipeManual, List<IngredientDto> ingredientDtoList) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", currentUserEmail)));
        Recipe recipe = new Recipe();
        recipe.setName(recipeName);
        recipe.setManual(recipeManual);
        recipe.setUser(user);
        for(IngredientDto ingredientDto: ingredientDtoList) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientDto.getName());
            ingredient.setUnit(ingredientDto.getUnit());
            ingredient.setCount(ingredientDto.getCount());
            ingredient.setRecipe(recipe);
            ingredientRepository.save(ingredient);
        }
        recipeRepository.save(recipe);
    }

    public RecipeDto findRecipeById(long id) throws ChangeSetPersister.NotFoundException {
        return recipeRepository.findById(id).map(RecipeDtoMapper::map).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    @Transactional
    public void saveEdited(long recipeId, RecipeDto recipeDto) throws ChangeSetPersister.NotFoundException {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        recipe.setName(recipeDto.getName());
        recipe.setManual(recipe.getManual());
        for (IngredientDto ingredientDto: recipeDto.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findById(ingredientDto.getId()).orElseThrow(ChangeSetPersister.NotFoundException::new);
            ingredient.setName(ingredientDto.getName());
            ingredient.setCount(ingredientDto.getCount());
            ingredient.setUnit(ingredientDto.getUnit());
            ingredientRepository.save(ingredient);
        }
        recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipeById(long id) throws ChangeSetPersister.NotFoundException {
        recipeRepository.delete(recipeRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
    }
}
