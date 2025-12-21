package ecom_blog.controller;

import ecom_blog.dto.RegisterUserDto;
import ecom_blog.mapper.UserMapper;
import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    // ✅ Page de connexion
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // → templates/login.html
    }

    // ✅ Page d’inscription
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return "user/inscription";
    }

    // ✅ Traitement du formulaire d’inscription
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterUserDto dto, BindingResult bindingResult,
            Model model) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
                return "user/inscription";
            }

            // 🔒 Vérifie si l’utilisateur existe déjà (par email)
            boolean emailExists = userService.getAllUsers()
                    .stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()));

            if (emailExists) {
                model.addAttribute("error", "❌ Cet email est déjà utilisé.");
                return "user/inscription";
            }

            User user = userMapper.toEntity(dto);

            // 🔑 Définit le rôle par défaut
            user.setRole(Role.ROLE_USER);

            // 🔒 Encode le mot de passe avant sauvegarde
            // user.setPassword(passwordEncoder.encode(user.getPassword()));

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
