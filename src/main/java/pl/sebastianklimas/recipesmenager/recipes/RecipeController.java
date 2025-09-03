package pl.sebastianklimas.recipesmenager.recipes;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.Recipe;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.RecipeRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    public List<RecipeDto> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable(name = "id") Long id) {
        RecipeDto recipeById = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipeById);
    }

    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(
            @Valid @RequestBody RecipeDto recipeDto,
            UriComponentsBuilder uriBuilder) {

        RecipeDto recipe = recipeService.createRecipe(recipeDto);

        URI uri = uriBuilder.path("/recipes/{id}").buildAndExpand(recipe.getId()).toUri();

        return ResponseEntity.created(uri).body(recipeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody RecipeDto recipeDto) {
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
}
