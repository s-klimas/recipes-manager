//package pl.sebastianklimas.recipesmenager.web;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import pl.sebastianklimas.recipesmenager.domain.user.UserService;
//import pl.sebastianklimas.recipesmenager.domain.user.dto.UserRegistrationDto;
//
//import javax.management.relation.RoleNotFoundException;
//
//@Controller
//public class RegistrationController {
//
//    private final UserService userService;
//
//    public RegistrationController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/register")
//    public String registrationForm(Model model) {
//        UserRegistrationDto userRegistration = new UserRegistrationDto();
//        model.addAttribute("user", userRegistration);
//        return "registration-form";
//    }
//
//    @PostMapping("/register")
//    public String register(UserRegistrationDto userRegistration) {
//        try {
//            userService.registerUserWithDefaultRole(userRegistration);
//        } catch (RoleNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return "redirect:/login";
//    }
//}
