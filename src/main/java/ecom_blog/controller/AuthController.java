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

    private final ecom_blog.repository.PrestataireRepository prestataireRepository;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, UserMapper userMapper,
            ecom_blog.repository.PrestataireRepository prestataireRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.prestataireRepository = prestataireRepository;
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
        System.out.println("DEBUG REGISTER: " + dto.toString()); // DEBUG LOG
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

            // 🔑 GESTION DES RÔLES ET CRÉATION PRESTATAIRE
            if ("PRESTATAIRE".equals(dto.getAccountType())) {
                user.setRole(Role.ROLE_PRESTATAIRE);
                // Sauvegarder d'abord l'utilisateur pour avoir l'ID
                User savedUser = userService.saveUser(user);

                // Créer l'entité Prestataire liée
                ecom_blog.model.Prestataire prestataire = new ecom_blog.model.Prestataire();
                prestataire.setUser(savedUser);
                prestataire.setNom(dto.getNom()); // On utilise le même nom par défaut
                prestataire.setTelephone(dto.getTelephone()); // ✅ CORRECTIF : Ajout du téléphone obligatoire

                try {
                    if (dto.getTypePrestataire() != null) {
                        prestataire.setTypePrestataire(
                                ecom_blog.model.enums.TypePrestataire.valueOf(dto.getTypePrestataire()));
                    } else {
                        prestataire.setTypePrestataire(ecom_blog.model.enums.TypePrestataire.AUTRE);
                    }
                } catch (Exception e) {
                    prestataire.setTypePrestataire(ecom_blog.model.enums.TypePrestataire.AUTRE);
                }

                prestataire.setDisponible(true); // Disponible par défaut
                prestataireRepository.save(prestataire);

                return "redirect:/login?success=PRESTATAIRE"; // Indicateur visuel pour debug

            } else {
                user.setRole(Role.ROLE_USER);
                userService.saveUser(user);
                return "redirect:/login?success=CLIENT";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l’inscription : " + e.getMessage());
            return "user/inscription";
        }
    }
}
