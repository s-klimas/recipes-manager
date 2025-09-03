package pl.sebastianklimas.recipesmenager.web;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientsListWrapperDto;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.RecipeServiceOld;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.dto.RecipeDto;

import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeControllerOld {

    private final RecipeServiceOld recipeService;

    public RecipeControllerOld(RecipeServiceOld recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public String recipe(@PathVariable long id, Model model) {
        try {
            RecipeDto recipeDto = recipeService.findRecipeById(id);
            List<IngredientDto> ingredientDtoList = recipeDto.getIngredients().stream().toList();
            IngredientsListWrapperDto ilwd = new IngredientsListWrapperDto();
            ilwd.setIngredientDtoList(ingredientDtoList);
            model.addAttribute("recipe", recipeDto);
            model.addAttribute("ilwd", ilwd);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
        return "recipe";
    }

    @PostMapping("/{id}")
    public String saveRecipe(@PathVariable long id, RecipeDto recipeDto, IngredientsListWrapperDto ilwd) {
        try {
            recipeService.saveEdited(id, recipeDto);
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/recipes/" + id;
    }

    @PostMapping("delete/{id}")
    public String deleteRecipe(@PathVariable long id) throws ChangeSetPersister.NotFoundException {
        recipeService.deleteRecipeById(id);
        return "redirect:/";
    }
}
