package pl.sebastianklimas.recipesmenager.recipes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeRequestDto;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeResponseDto;

import java.util.List;

@AllArgsConstructor
@Service
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;

    public RecipeResponseDto createRecipe(RecipeRequestDto recipeDto) {
        Recipe recipe = recipeMapper.toEntity(recipeDto);
        recipe.setVisibility(Visibility.PRIVATE.toString());
        recipeRepository.save(recipe);

        return recipeMapper.toDto(recipe);
    }

    public List<RecipeResponseDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipeMapper::toDto)
                .toList();
    }

    public RecipeResponseDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        return recipeMapper.toDto(recipe);
    }

    public RecipeResponseDto updateRecipe(Long id, RecipeRequestDto recipeDto) {
        Recipe recipe =  recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        recipeMapper.update(recipeDto, recipe);
        recipeRepository.save(recipe);

        return recipeMapper.toDto(recipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        recipeRepository.delete(recipe);
    }
}
