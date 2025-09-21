package pl.sebastianklimas.recipesmenager.domain.recipeOld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.Ingredient;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.IngredientRepository;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.dto.RecipeDto;
import pl.sebastianklimas.recipesmenager.domain.user.User;
import pl.sebastianklimas.recipesmenager.domain.user.UserRepository;
import pl.sebastianklimas.recipesmenager.recipes.Recipe;
import pl.sebastianklimas.recipesmenager.recipes.RecipeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock UserRepository userRepository;
    @Mock IngredientRepository ingredientRepository;

    @InjectMocks
    RecipeServiceOld recipeService;

    @BeforeEach
    public void init() {
        recipeRepository = mock(RecipeRepository.class);
        userRepository = mock(UserRepository.class);
        ingredientRepository = mock(IngredientRepository.class);
        recipeService = new RecipeServiceOld(recipeRepository, userRepository, ingredientRepository);
    }

    @Test
    public void testing_FindAllRecipesByUserId_WhenRecipesExist_ShouldReturnListOfRecipeListDto() {
        // given
        Long userId = 1L;
        Recipe recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName("Recipe 1");
        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Recipe 2");
        Set<Recipe> mockRecipes = Set.of(recipe1, recipe2);

        // when
        when(recipeRepository.findAllByUserId(userId)).thenReturn(mockRecipes);
        List<RecipeDto> result = recipeService.findAllRecipesByUserId(userId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(mockRecipes.size());
    }

    @Test
    public void testing_FindAllRecipesByUserId_WhenNoRecipesExist_ShouldReturnEmptySet() {
        // given
        Long userId = 1L;

        // when
        when(recipeRepository.findAllByUserId(userId)).thenReturn(Set.of());
        List<RecipeDto> result = recipeService.findAllRecipesByUserId(userId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void testing_AddRecipeWithIngredients_WhenUserExists_ShouldAddRecipeWithIngredients() {
        // given
        String currentUserEmail = "test@example.com";
        String recipeName = "Makaron z sosem";
        String recipeManual = "Ugotuj makaron i dodaj sos";
        List<IngredientDto> ingredientDtoList = Arrays.asList(
                new IngredientDto(1L, "Makaron", 200, "g"),
                new IngredientDto(2L, "Sos", 100, "ml")
        );

        User mockUser = new User();
        mockUser.setEmail(currentUserEmail);

        // when
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.of(mockUser));
        recipeService.addRecipeWithIngredients(currentUserEmail, recipeName, recipeManual, ingredientDtoList);

        // then
        verify(recipeRepository, times(1)).save(any(Recipe.class));
        verify(ingredientRepository, times(2)).save(any(Ingredient.class));
    }

    @Test
    public void testing_AddRecipeWithIngredients_WhenUserDoesNotExist_ShouldThrowException() {
        // given
        String currentUserEmail = "test@example.com";
        String recipeName = "Makaron z sosem";
        String recipeManual = "Ugotuj makaron i dodaj sos";
        List<IngredientDto> ingredientDtoList = Arrays.asList(
                new IngredientDto(1L, "Makaron", 200, "g"),
                new IngredientDto(2L, "Sos", 100, "ml")
        );

        // when
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                recipeService.addRecipeWithIngredients(currentUserEmail, recipeName, recipeManual, ingredientDtoList));

        // then
        verify(recipeRepository, never()).save(any(Recipe.class));
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

}