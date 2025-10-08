package pl.sebastianklimas.recipesmenager.recipes;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sebastianklimas.recipesmenager.auth.AuthService;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeRequestDto;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeResponseDto;
import pl.sebastianklimas.recipesmenager.recipes.exceptions.RecipeNotAvailableForCurrentUserException;
import pl.sebastianklimas.recipesmenager.recipes.exceptions.RecipeNotFoundException;
import pl.sebastianklimas.recipesmenager.recipes.ingredients.Ingredient;
import pl.sebastianklimas.recipesmenager.users.User;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DisplayName("RecipeService Unit Tests")
class RecipeServiceTest {

    @Mock private AuthService authService;
    @Mock private RecipeRepository recipeRepository;
    @Mock private RecipeMapper recipeMapper;

    @InjectMocks private RecipeService recipeService;

    @Captor
    private ArgumentCaptor<Recipe> recipeCaptor;

    private RecipeRequestDto recipeRequestDto;
    private RecipeResponseDto recipeResponseDto;
    private Recipe recipeEntity;
    private User currentUser;
    private User anotherUser;
    private Recipe recipeUserPrivate;
    private Recipe recipeUserPublic;
    private Recipe recipeOtherPrivate;
    private Recipe recipeOtherPublic;
    private Recipe recipe;
    private Recipe anotherRecipe;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        anotherUser = new User();

        recipeUserPrivate = createRecipe(currentUser, Visibility.PRIVATE);
        recipeUserPublic = createRecipe(currentUser, Visibility.PUBLIC);
        recipeOtherPrivate = createRecipe(anotherUser, Visibility.PRIVATE);
        recipeOtherPublic = createRecipe(anotherUser, Visibility.PUBLIC);

        recipe = createRecipe(currentUser, Visibility.PRIVATE);
        anotherRecipe = createRecipe(anotherUser, Visibility.PRIVATE);

        recipeEntity = createRecipe(currentUser, Visibility.PRIVATE);
        recipeEntity.setIngredients(Set.of(new Ingredient(), new Ingredient()));

        recipeRequestDto = new RecipeRequestDto();
        recipeResponseDto = new RecipeResponseDto();
    }

    private Recipe createRecipe(User user, Visibility visibility) {
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setVisibility(visibility.toString());
        return recipe;
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Nested
    @DisplayName("Create Recipe")
    class CreateRecipeTests {

        @Test
        @DisplayName("Should create recipe successfully")
        void shouldCreateRecipe() {
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toEntity(recipeRequestDto)).thenReturn(recipeEntity);
            when(recipeMapper.toDto(recipeEntity)).thenReturn(recipeResponseDto);
            when(recipeRepository.save(any())).thenReturn(recipeEntity);

            RecipeResponseDto result = recipeService.createRecipe(recipeRequestDto);

            assertThat(result).isEqualTo(recipeResponseDto);
            verify(authService).getCurrentUser();
            verify(recipeRepository).save(recipeEntity);
            verify(recipeMapper).toDto(recipeEntity);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(recipeEntity.getUser()).isEqualTo(currentUser);
                softly.assertThat(recipeEntity.getVisibility()).isEqualTo(Visibility.PRIVATE.toString());
                softly.assertThat(recipeEntity.getIngredients())
                        .allMatch(i -> i.getRecipe().equals(recipeEntity));
            });
        }

        @Test
        @DisplayName("Should pass correct recipe to repository")
        void shouldPassCorrectRecipeToRepository() {
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toEntity(recipeRequestDto)).thenReturn(recipeEntity);
            when(recipeMapper.toDto(any())).thenReturn(recipeResponseDto);

            recipeService.createRecipe(recipeRequestDto);

            verify(recipeRepository).save(recipeCaptor.capture());
            Recipe saved = recipeCaptor.getValue();

            assertThat(saved)
                    .extracting(Recipe::getUser, Recipe::getVisibility)
                    .containsExactly(currentUser, Visibility.PRIVATE.toString());
            assertThat(recipe.getIngredients())
                    .asInstanceOf(InstanceOfAssertFactories.iterable(Ingredient.class))
                    .allSatisfy(ing -> assertThat(ing.getRecipe()).isEqualTo(recipe));
        }
    }

    // ============================================================
    // READ (getAllRecipes & getRecipeById)
    // ============================================================

    @Nested
    @DisplayName("Get Recipes")
    class GetRecipesTests {

        @Test
        @DisplayName("Should return user and public recipes")
        void shouldReturnUserAndPublicRecipes() {
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeRepository.findAll()).thenReturn(List.of(
                    recipeUserPrivate, recipeUserPublic, recipeOtherPrivate, recipeOtherPublic));
            when(recipeMapper.toDto(any())).thenReturn(new RecipeResponseDto());

            List<RecipeResponseDto> result = recipeService.getAllRecipes();

            assertThat(result.size()).isEqualTo(3);
            verify(recipeMapper, times(3)).toDto(any());
        }

        @Test
        @DisplayName("Should not return private recipes of other users")
        void shouldNotReturnPrivateRecipesOfOtherUsers() {
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeRepository.findAll()).thenReturn(List.of(recipeOtherPrivate));

            List<RecipeResponseDto> result = recipeService.getAllRecipes();

            assertThat(result.isEmpty()).isTrue();
            verify(recipeMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should return empty list when no recipes")
        void shouldReturnEmptyListWhenNoRecipes() {
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeRepository.findAll()).thenReturn(Collections.emptyList());

            List<RecipeResponseDto> result = recipeService.getAllRecipes();

            assertThat(result.isEmpty()).isTrue();
            verify(recipeMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should call mapper only for filtered recipes")
        void shouldCallMapperOnlyForFilteredRecipes() {
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeRepository.findAll()).thenReturn(List.of(
                    recipeUserPrivate, recipeOtherPrivate, recipeOtherPublic));
            when(recipeMapper.toDto(any())).thenReturn(new RecipeResponseDto());

            recipeService.getAllRecipes();

            verify(recipeMapper, times(2)).toDto(any());
        }

        @Test
        @DisplayName("Should return recipe when current user is owner")
        void shouldReturnRecipeWhenCurrentUserIsOwner() {
            Long id = 10L;
            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

            RecipeResponseDto result = recipeService.getRecipeById(id);

            assertThat(result).isEqualTo(recipeResponseDto);
            verify(recipeRepository).findById(id);
            verify(recipeMapper).toDto(recipe);
        }

        @Test
        @DisplayName("Should return recipe when it is public and user is different")
        void shouldReturnRecipeWhenItIsPublicAndUserIsDifferent() {
            Long id = 20L;
            recipe.setUser(anotherUser);
            recipe.setVisibility(Visibility.PUBLIC.toString());

            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

            RecipeResponseDto result = recipeService.getRecipeById(id);

            assertThat(result).isEqualTo(recipeResponseDto);
            verify(recipeMapper).toDto(recipe);
        }

        @Test
        @DisplayName("Should throw when recipe not found")
        void shouldThrowWhenRecipeNotFound() {
            Long id = 99L;
            when(recipeRepository.findById(id)).thenReturn(Optional.empty());

            assertThatExceptionOfType(RecipeNotFoundException.class)
                    .isThrownBy(() -> recipeService.getRecipeById(id))
                    .withMessage("Recipe not found");

            verify(recipeMapper, never()).toDto(any());
        }

        @Test
        @DisplayName("Should throw when recipe is private and user is not owner")
        void shouldThrowWhenRecipeIsPrivateAndUserIsNotOwner() {
            Long id = 30L;
            recipe.setUser(anotherUser);
            recipe.setVisibility(Visibility.PRIVATE.toString());

            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);

            assertThatExceptionOfType(RecipeNotAvailableForCurrentUserException.class)
                    .isThrownBy(() -> recipeService.getRecipeById(id))
                    .withMessage("Recipe does not belong to current user and is not public");

            verify(recipeMapper, never()).toDto(any());
        }
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Nested
    @DisplayName("Update Recipe")
    class UpdateRecipeTests {

        @Test
        @DisplayName("Should update recipe when user is owner")
        void shouldUpdateRecipeWhenUserIsOwner() {
            Long id = 1L;
            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

            RecipeResponseDto result = recipeService.updateRecipe(id, recipeRequestDto);

            assertThat(result).isEqualTo(recipeResponseDto);
            verify(recipeMapper).update(recipeRequestDto, recipe);
            verify(recipeRepository).save(recipe);
            verify(recipeMapper).toDto(recipe);

            assertThat(recipe.getIngredients())
                    .asInstanceOf(InstanceOfAssertFactories.iterable(Ingredient.class))
                    .allSatisfy(ing -> assertThat(ing.getRecipe()).isEqualTo(recipe));
        }

        @Test
        @DisplayName("Should throw when recipe not found")
        void shouldThrowWhenRecipeNotFoundWhileUpdating() {
            Long id = 99L;
            when(recipeRepository.findById(id)).thenReturn(Optional.empty());

            assertThatExceptionOfType(RecipeNotFoundException.class)
                    .isThrownBy(() -> recipeService.updateRecipe(id, recipeRequestDto))
                    .withMessage("Recipe not found");

            verify(recipeRepository).findById(id);
            verifyNoInteractions(recipeMapper);
        }

        @Test
        @DisplayName("Should throw when user is not owner")
        void shouldThrowWhenUserIsNotOwner() {
            Long id = 5L;
            when(recipeRepository.findById(id)).thenReturn(Optional.of(anotherRecipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);

            assertThatExceptionOfType(RecipeNotAvailableForCurrentUserException.class)
                    .isThrownBy(() -> recipeService.updateRecipe(id, recipeRequestDto))
                    .withMessage("Recipe does not belong to current user");

            verify(recipeMapper, never()).update(any(), any());
            verify(recipeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should clear ingredients before update")
        void shouldClearIngredientsBeforeUpdate() {
            Long id = 2L;
            recipe.setIngredients(new HashSet<>(List.of(new Ingredient(), new Ingredient())));

            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

            recipeService.updateRecipe(id, recipeRequestDto);

            assertThat(recipe.getIngredients()).isNotNull();
            verify(recipeMapper).update(recipeRequestDto, recipe);
        }

        @Test
        @DisplayName("Should save and map after successful update")
        void shouldSaveAndMapAfterSuccessfulUpdate() {
            Long id = 3L;
            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);
            when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

            RecipeResponseDto result = recipeService.updateRecipe(id, recipeRequestDto);

            assertThat(result).isNotNull();
            verify(recipeRepository).save(recipe);
            verify(recipeMapper).toDto(recipe);
        }
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Nested
    @DisplayName("Delete Recipe")
    class DeleteRecipeTests {

        @Test
        @DisplayName("Should delete recipe when user is owner")
        void shouldDeleteRecipeWhenUserIsOwner() {
            Long id = 1L;
            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);

            recipeService.deleteRecipe(id);

            verify(recipeRepository).delete(recipe);
        }

        @Test
        @DisplayName("Should throw when recipe not found while deleting")
        void shouldThrowWhenRecipeNotFoundWhileDeleting() {
            Long id = 42L;
            when(recipeRepository.findById(id)).thenReturn(Optional.empty());

            assertThatExceptionOfType(RecipeNotFoundException.class)
                    .isThrownBy(() -> recipeService.deleteRecipe(id))
                    .withMessage("Recipe not found");

            verify(recipeRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should throw when user is not owner while deleting")
        void shouldThrowWhenUserIsNotOwnerWhileDeleting() {
            Long id = 2L;
            recipe.setUser(anotherUser);

            when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
            when(authService.getCurrentUser()).thenReturn(currentUser);

            assertThatExceptionOfType(RecipeNotAvailableForCurrentUserException.class)
                    .isThrownBy(() -> recipeService.deleteRecipe(id))
                    .withMessage("Recipe does not belong to current user");

            verify(recipeRepository, never()).delete(any());
        }
    }
}