package pl.sebastianklimas.recipesmenager.recipes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeRequestDto;
import pl.sebastianklimas.recipesmenager.recipes.dtos.RecipeResponseDto;
import pl.sebastianklimas.recipesmenager.recipes.exceptions.RecipeNotAvailableForCurrentUserException;
import pl.sebastianklimas.recipesmenager.recipes.exceptions.RecipeNotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "Gets all recipes available the the logged user.")
    public Page<RecipeResponseDto> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return recipeService.getAllRecipes(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets the recipe by ID.")
    public ResponseEntity<RecipeResponseDto> getRecipe(
            @PathVariable @Parameter(name = "id", description = "The ID of the recipe.") Long id
    ) {
        RecipeResponseDto recipeById = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipeById);
    }

    @PostMapping
    @Operation(summary = "Adds a recipe to database.")
    public ResponseEntity<RecipeResponseDto> createRecipe(
            @Parameter(description = "The recipe to add.")
            @Valid @RequestBody RecipeRequestDto recipeDto,
            UriComponentsBuilder uriBuilder) {

        RecipeResponseDto recipe = recipeService.createRecipe(recipeDto);

        URI uri = uriBuilder.path("/recipes/{id}").buildAndExpand(recipe.getId()).toUri();

        return ResponseEntity.created(uri).body(recipe);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates the recipe.")
    public ResponseEntity<RecipeResponseDto> updateRecipe(
            @PathVariable @Parameter(description = "The ID of the recipe to update.") Long id,
            @Parameter(description = "New recipe data.")
            @Valid @RequestBody RecipeRequestDto recipeDto) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipeDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes the recipe.")
    public ResponseEntity<?> deleteRecipe(
            @PathVariable @Parameter(description = "The ID of the recipe to delete.") Long id
    ) {
        recipeService.deleteRecipe(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<String> handleRecipeNotFoundException(RecipeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RecipeNotAvailableForCurrentUserException.class)
    public ResponseEntity<String> handleRecipeNotAvailableForCurrentUserException(RecipeNotAvailableForCurrentUserException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
