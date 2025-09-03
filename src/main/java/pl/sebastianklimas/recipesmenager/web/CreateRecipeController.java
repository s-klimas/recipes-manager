package pl.sebastianklimas.recipesmenager.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientDto;
import pl.sebastianklimas.recipesmenager.domain.ingredient.dto.IngredientsListWrapperDto;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.RecipeServiceOld;
import pl.sebastianklimas.recipesmenager.domain.recipeOld.dto.RecipeAddDto;

@Controller
public class CreateRecipeController {

    private final RecipeServiceOld recipeService;

    public CreateRecipeController(RecipeServiceOld recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/add-recipe")
    public String addRecipeForm(Model model) {
        IngredientsListWrapperDto ilwd = new IngredientsListWrapperDto();
        ilwd.addIngredientToList(new IngredientDto());
        model.addAttribute("ilwd", ilwd);
        RecipeAddDto recipeAddDto = new RecipeAddDto();
        model.addAttribute("recipeAddDto", recipeAddDto);
        return "add-recipe";
    }

    @PostMapping("/add-recipe")
    public String addRecipe(RecipeAddDto recipeAddDto,
                            IngredientsListWrapperDto ilwd,
                            Authentication authentication) {
        String currentUserEmail = authentication.getName();
        recipeService.addRecipeWithIngredients(currentUserEmail, recipeAddDto.getName(), recipeAddDto.getManual(),ilwd.getIngredientDtoList());
        return "redirect:/";
    }
}
