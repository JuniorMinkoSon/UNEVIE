package ecom_blog.controller;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ✅ PAGE LOGIN (MANQUANTE)
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    // ✅ PAGE INSCRIPTION
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "user/inscription";
    }

    // ✅ TRAITEMENT INSCRIPTION
    @PostMapping("/register")
    public String registerSubmit(
            @ModelAttribute("user") User user,
            @RequestParam("type") String type,
            Model model
    ) {

        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Cet email existe déjà");
            return "user/inscription";
        }

        // 🔑 RÔLE SELON LE CHOIX UTILISATEUR
        if ("LIVREUR".equals(type)) {
            user.setRole(Role.ROLE_LIVREUR);
        } else {
            user.setRole(Role.ROLE_USER);
        }

        userService.saveUser(user);

        return "redirect:/login?success";
    }
}
