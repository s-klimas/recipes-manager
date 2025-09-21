package pl.sebastianklimas.recipesmenager.recipes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeDto;

import java.util.List;

@AllArgsConstructor
@Service
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;

    public RecipeDto createRecipe(RecipeDto recipeDto) {
        Recipe recipe = recipeMapper.toEntity(recipeDto);
        recipeRepository.save(recipe);
        recipeDto.setId(recipe.getId());

        return recipeDto;
    }

    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipeMapper::toDto)
                .toList();
    }

    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        return recipeMapper.toDto(recipe);
    }

    public RecipeDto updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe recipe =  recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));

        recipeMapper.update(recipeDto, recipe);
        recipeRepository.save(recipe);

        recipeDto.setId(recipe.getId());

        return recipeDto;
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        recipeRepository.delete(recipe);
    }
}
