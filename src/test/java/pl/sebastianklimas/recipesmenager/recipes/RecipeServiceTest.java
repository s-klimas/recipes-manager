package pl.sebastianklimas.recipesmenager.recipes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @Mock
    private AuthService authService;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeService recipeService;

    private RecipeRequestDto recipeRequestDto;
    private Recipe recipeEntity;
    private RecipeResponseDto recipeResponseDto;
    private User currentUser;
    private User anotherUser;
    private Recipe recipeUserPrivate;
    private Recipe recipeUserPublic;
    private Recipe recipeOtherPrivate;
    private Recipe recipeOtherPublic;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipeRequestDto = new RecipeRequestDto();
        recipeResponseDto = new RecipeResponseDto();
        currentUser = new User();

        Ingredient ing1 = new Ingredient();
        Ingredient ing2 = new Ingredient();
        recipeEntity = new Recipe();
        recipeEntity.setIngredients(Set.of(ing1, ing2));

        recipeUserPrivate = new Recipe();
        recipeUserPrivate.setUser(currentUser);
        recipeUserPrivate.setVisibility(Visibility.PRIVATE.toString());

        recipeUserPublic = new Recipe();
        recipeUserPublic.setUser(currentUser);
        recipeUserPublic.setVisibility(Visibility.PUBLIC.toString());

        recipeOtherPrivate = new Recipe();
        recipeOtherPrivate.setUser(anotherUser);
        recipeOtherPrivate.setVisibility(Visibility.PRIVATE.toString());

        recipeOtherPublic = new Recipe();
        recipeOtherPublic.setUser(anotherUser);
        recipeOtherPublic.setVisibility(Visibility.PUBLIC.toString());

        recipe = new Recipe();
        recipe.setUser(currentUser);
        recipe.setVisibility(Visibility.PRIVATE.toString());

        recipeResponseDto = new RecipeResponseDto();
    }

    @Test
    void shouldCreateRecipe() {
        // given
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toEntity(recipeRequestDto)).thenReturn(recipeEntity);
        when(recipeMapper.toDto(recipeEntity)).thenReturn(recipeResponseDto);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipeEntity);

        // when
        RecipeResponseDto result = recipeService.createRecipe(recipeRequestDto);

        // then
        assertThat(result).isEqualTo(recipeResponseDto);

        verify(authService).getCurrentUser();
        verify(recipeMapper).toEntity(recipeRequestDto);
        verify(recipeRepository).save(recipeEntity);
        verify(recipeMapper).toDto(recipeEntity);

        assertThat(recipeEntity.getVisibility()).isEqualTo(Visibility.PRIVATE.toString());
        assertThat(recipeEntity.getUser()).isEqualTo(currentUser);
        recipeEntity.getIngredients().forEach(
                ing -> assertThat(ing.getRecipe()).isEqualTo(recipeEntity)
        );
    }

    @Test
    void shouldPassCorrectRecipeToRepository() {
        // given
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toEntity(recipeRequestDto)).thenReturn(recipeEntity);
        when(recipeMapper.toDto(any())).thenReturn(recipeResponseDto);

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);

        // when
        recipeService.createRecipe(recipeRequestDto);

        // then
        verify(recipeRepository).save(captor.capture());
        Recipe saved = captor.getValue();

        assertThat(saved.getVisibility()).isEqualTo(Visibility.PRIVATE.toString());
        assertThat(saved.getUser()).isEqualTo(currentUser);
        saved.getIngredients().forEach(
                ing -> assertThat(ing.getRecipe()).isEqualTo(recipeEntity)
        );
    }

    @Test
    void shouldReturnUserAndPublicRecipes() {
        // given
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeRepository.findAll()).thenReturn(List.of(
                recipeUserPrivate, recipeUserPublic, recipeOtherPrivate, recipeOtherPublic
        ));

        when(recipeMapper.toDto(any())).thenAnswer(invocation -> new RecipeResponseDto());

        // when
        List<RecipeResponseDto> result = recipeService.getAllRecipes();

        // then
        assertThat(result.size()).isEqualTo(3);
        verify(recipeMapper, times(3)).toDto(any(Recipe.class));
        verify(recipeRepository).findAll();
        verify(authService).getCurrentUser();
    }

    @Test
    void shouldNotReturnPrivateRecipesOfOtherUsers() {
        // given
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeRepository.findAll()).thenReturn(List.of(recipeOtherPrivate));

        // when
        List<RecipeResponseDto> result = recipeService.getAllRecipes();

        // then
        assertThat(result.isEmpty()).isTrue();
        verify(recipeMapper, never()).toDto(any());
    }

    @Test
    void shouldReturnEmptyListWhenNoRecipes() {
        // given
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeRepository.findAll()).thenReturn(List.of());

        // when
        List<RecipeResponseDto> result = recipeService.getAllRecipes();

        // then
        assertThat(result.isEmpty()).isTrue();
        verify(recipeMapper, never()).toDto(any());
    }

    @Test
    void shouldCallMapperOnlyForFilteredRecipes() {
        // given
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeRepository.findAll()).thenReturn(List.of(
                recipeUserPrivate, recipeOtherPrivate, recipeOtherPublic
        ));
        when(recipeMapper.toDto(any())).thenAnswer(invocation -> new RecipeResponseDto());

        // when
        recipeService.getAllRecipes();

        // then
        verify(recipeMapper, times(2)).toDto(any());
    }

    @Test
    void shouldReturnRecipeWhenCurrentUserIsOwner() {
        // given
        Long id = 10L;
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

        // when
        RecipeResponseDto result = recipeService.getRecipeById(id);

        // then
        assertThat(result).isEqualTo(recipeResponseDto);
        verify(recipeRepository).findById(id);
        verify(recipeMapper).toDto(recipe);
    }

    @Test
    void shouldReturnRecipeWhenItIsPublicAndUserIsDifferent() {
        // given
        Long id = 20L;
        recipe.setUser(anotherUser);
        recipe.setVisibility(Visibility.PUBLIC.toString());

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

        // when
        RecipeResponseDto result = recipeService.getRecipeById(id);

        // then
        assertThat(result).isEqualTo(recipeResponseDto);
        verify(recipeMapper).toDto(recipe);
    }

    @Test
    void shouldThrowWhenRecipeNotFoundWhileFinding() {
        // given
        Long id = 99L;
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> recipeService.getRecipeById(id))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("Recipe not found");

        verify(recipeMapper, never()).toDto(any());
    }

    @Test
    void shouldThrowWhenRecipeIsPrivateAndUserIsNotOwner() {
        // given
        Long id = 30L;
        recipe.setUser(anotherUser);
        recipe.setVisibility(Visibility.PRIVATE.toString());

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);

        // when / then
        assertThatThrownBy(() -> recipeService.getRecipeById(id))
                .isInstanceOf(RecipeNotAvailableForCurrentUserException.class)
                .hasMessage("Recipe does not belong to current user and is not public");

        verify(recipeMapper, never()).toDto(any());
    }

    @Test
    void shouldUpdateRecipeWhenUserIsOwner() {
        // given
        Long id = 1L;
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

        // when
        RecipeResponseDto result = recipeService.updateRecipe(id, recipeRequestDto);

        // then
        assertThat(result).isEqualTo(recipeResponseDto);

        verify(recipeMapper).update(recipeRequestDto, recipe);
        verify(recipeRepository).save(recipe);
        verify(recipeMapper).toDto(recipe);

        recipe.getIngredients().forEach(ingredient ->
                assertThat(ingredient.getRecipe()).isEqualTo(recipe)
        );
    }

    @Test
    void shouldThrowWhenRecipeNotFoundWhileUpdatingRecipe() {
        // given
        Long id = 99L;
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> recipeService.updateRecipe(id, recipeRequestDto))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("Recipe not found");

        verify(recipeRepository).findById(id);
        verifyNoInteractions(recipeMapper);
    }

    @Test
    void shouldThrowWhenUserIsNotOwner() {
        // given
        Long id = 5L;
        recipe.setUser(anotherUser);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);

        // when / then
        assertThatThrownBy(() -> recipeService.updateRecipe(id, recipeRequestDto))
                .isInstanceOf(RecipeNotAvailableForCurrentUserException.class)
                .hasMessage("Recipe does not belong to current user");

        verify(recipeRepository).findById(id);
        verify(recipeMapper, never()).update(any(), any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void shouldClearIngredientsBeforeUpdate() {
        // given
        Long id = 2L;
        Set<Ingredient> oldIngredients = new HashSet<>(List.of(new Ingredient(), new Ingredient()));
        recipe.setIngredients(oldIngredients);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

        // when
        recipeService.updateRecipe(id, recipeRequestDto);

        // then
        assertThat(recipe.getIngredients()).isNotNull();
        verify(recipeMapper).update(recipeRequestDto, recipe);
    }

    @Test
    void shouldSaveAndMapAfterSuccessfulUpdate() {
        // given
        Long id = 3L;
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(recipeMapper.toDto(recipe)).thenReturn(recipeResponseDto);

        // when
        RecipeResponseDto result = recipeService.updateRecipe(id, recipeRequestDto);

        // then
        assertThat(result).isNotNull();
        verify(recipeRepository, times(1)).save(recipe);
        verify(recipeMapper, times(1)).toDto(recipe);
    }

    @Test
    void shouldDeleteRecipeWhenUserIsOwner() {
        // given
        Long id = 1L;
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);

        // when
        recipeService.deleteRecipe(id);

        // then
        verify(recipeRepository).findById(id);
        verify(recipeRepository).delete(recipe);
    }

    @Test
    void shouldThrowWhenRecipeNotFoundWhileDeleting() {
        // given
        Long id = 42L;
        when(recipeRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> recipeService.deleteRecipe(id))
                .isInstanceOf(RecipeNotFoundException.class)
                .hasMessage("Recipe not found");

        verify(recipeRepository).findById(id);
        verify(recipeRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWhenUserIsNotOwnerWhileDeleting() {
        // given
        Long id = 2L;
        recipe.setUser(anotherUser);

        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        when(authService.getCurrentUser()).thenReturn(currentUser);

        // when / then
        assertThatThrownBy(() -> recipeService.deleteRecipe(id))
                .isInstanceOf(RecipeNotAvailableForCurrentUserException.class)
                .hasMessage("Recipe does not belong to current user");

        verify(recipeRepository).findById(id);
        verify(recipeRepository, never()).delete(any());
    }
}