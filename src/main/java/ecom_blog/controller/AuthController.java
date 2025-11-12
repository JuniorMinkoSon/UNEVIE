package ecom_blog.controller;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Page de connexion
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // → templates/login.html
    }

    // ✅ Page d’inscription
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "user/inscription"; // → templates/user/inscription.html
    }

    // ✅ Traitement du formulaire d’inscription
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            // 🔒 Vérifie si l’utilisateur existe déjà (par email)
            boolean emailExists = userService.getAllUsers()
                    .stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));

            if (emailExists) {
                model.addAttribute("error", "❌ Cet email est déjà utilisé.");
                return "user/inscription";
            }

            // 🔑 Définit le rôle par défaut
            user.setRole(Role.ROLE_USER);

            // 🔒 Encode le mot de passe avant sauvegarde
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // 📞 Enregistre l’utilisateur (y compris téléphone)
            userService.saveUser(user);

            // ✅ Redirection vers login avec message de succès
            return "redirect:/login?success";

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l’inscription : " + e.getMessage());
            return "user/inscription";
        }
    }
}
