package pl.sebastianklimas.recipesmenager.recipes;

import lombok.AllArgsConstructor;
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
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeResponseDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipe(@PathVariable(name = "id") Long id) {
        RecipeResponseDto recipeById = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipeById);
    }

    @PostMapping
    public ResponseEntity<RecipeResponseDto> createRecipe(
            @Valid @RequestBody RecipeRequestDto recipeDto,
            UriComponentsBuilder uriBuilder) {

        RecipeResponseDto recipe = recipeService.createRecipe(recipeDto);

        URI uri = uriBuilder.path("/recipes/{id}").buildAndExpand(recipe.getId()).toUri();

        return ResponseEntity.created(uri).body(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> updateRecipe(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody RecipeRequestDto recipeDto) {
        return ResponseEntity.ok(recipeService.updateRecipe(id, recipeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable(name = "id") Long id) {
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
