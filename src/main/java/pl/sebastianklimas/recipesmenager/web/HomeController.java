package pl.sebastianklimas.recipesmenager.web;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import pl.sebastianklimas.recipesmenager.domain.fridge.FridgeService;
//import pl.sebastianklimas.recipesmenager.domain.fridge.dto.FridgeDto;
import pl.sebastianklimas.recipesmenager.domain.recipe.RecipeService;
import pl.sebastianklimas.recipesmenager.domain.recipe.dto.RecipeDto;
import pl.sebastianklimas.recipesmenager.domain.user.UserService;
import pl.sebastianklimas.recipesmenager.domain.user.dto.UserIdentifyDto;

import java.util.List;

@Controller
public class HomeController {

//    private final FridgeService fridgeService;
    private final UserService userService;
    private final RecipeService recipeService;

    public HomeController(UserService userService, RecipeService recipeService) {
//        this.fridgeService = fridgeService;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            UserIdentifyDto user = userService.findIdentifyDataByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
//            List<FridgeDto> fridges = fridgeService.findFridgesByUserId(user.getId());
//            model.addAttribute("fridges", fridges);

            List<RecipeDto> recipes = recipeService.findAllRecipesByUserId(user.getId());
            model.addAttribute("recipes", recipes);
        }
        return "home";
    }
}
