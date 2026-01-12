package ecom_blog.controller;

import ecom_blog.dto.UserDto;
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

    // ✅ Page de connexion utilisateur (standard)
    @GetMapping("/login")
    public String showUserLogin() {
        return "login"; // templates/login.html
    }

    // ❌ SUPPRIMÉ : plus de /admin/login ici
    // @GetMapping("/admin/login")
    // public String showAdminLogin() { return "admin/login"; }

    // ✅ Page d'inscription
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/inscription";
    }

    // ✅ Traitement du formulaire d'inscription
    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto userDto, Model model) {
        if (userService.findByEmail(userDto.getEmail()) != null) {
            model.addAttribute("error", "Cet email est déjà utilisé.");
            return "user/inscription";
        }
        userService.saveUser(userDto);
        return "redirect:/login?success";
    }

    // ✅ Page oubli de mot de passe
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "user/forgot-password";
    }

    // ✅ Traitement oubli de mot de passe (Draft)
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        model.addAttribute("message", "Un email a été envoyé si le compte existe.");
        return "user/forgot-password";
    }
}
