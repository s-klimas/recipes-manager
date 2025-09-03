//package pl.sebastianklimas.recipesmenager.web;
//
//import org.springframework.data.crossstore.ChangeSetPersister;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordRecoveryDto;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.PasswordRecoveryService;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.exceptions.TokenExpiredException;
//import pl.sebastianklimas.recipesmenager.domain.passwordRecovery.exceptions.TokenUsedException;
//import pl.sebastianklimas.recipesmenager.domain.user.UserService;
//
//@Controller
//public class PasswordRecoveryController {
//    private final PasswordRecoveryService prs;
//    private final UserService userService;
//
//    public PasswordRecoveryController(PasswordRecoveryService prs, UserService userService) {
//        this.prs = prs;
//        this.userService = userService;
//    }
//
//    @GetMapping("/forget-password")
//    public String forget() {
//        return "forget-password";
//    }
//
//    @PostMapping("/forget-password")
//    public String forgetPassword(@RequestParam String email) {
//        prs.forgetPassword(email);
//        return "redirect:/forget-password?send";
//    }
//
//    @GetMapping("/recovery")
//    public String recovery(@RequestParam String token, @RequestParam String email, Model model) {
//        PasswordRecoveryDto prd = new PasswordRecoveryDto();
//        prd.setToken(token);
//        prd.setEmail(email);
//        model.addAttribute("prd", prd);
//        return "recovery";
//    }
//
//    @PostMapping("/recovery")
//    public String recoveryPassword(PasswordRecoveryDto prd) throws ChangeSetPersister.NotFoundException {
//        try {
//            System.out.println(prd.getEmail());
//            System.out.println(prd.getToken());
//            System.out.println(prd.getPassword());
//            userService.validateAndChangePassword(prd);
//        } catch (TokenExpiredException e) {
//            return "redirect:/recovery?expired";
//        } catch (TokenUsedException e) {
//            return "redirect:/recovery?used";
//        }
//        return "redirect:/login";
//    }
//}
