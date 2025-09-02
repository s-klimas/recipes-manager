package pl.sebastianklimas.recipesmenager.api.web;

import org.springframework.web.bind.annotation.*;
import pl.sebastianklimas.recipesmenager.domain.recipe.Recipe;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @PostMapping("/add")
    public void addRecipe(@RequestBody Recipe recipe) {

    }

    @GetMapping("/{id}")
    public void getRecipe(@PathVariable(name = "id") int id) {

    }

    @PutMapping("/{id}")
    public void updateRecipe(@PathVariable(name = "id") int id, @RequestBody Recipe recipe) {

    }

    @DeleteMapping("/delete/{id}")
    public void deleteRecipe(@PathVariable(name = "id") int id) {

    }
}
