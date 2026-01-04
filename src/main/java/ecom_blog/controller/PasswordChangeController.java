package ecom_blog.controller;

import ecom_blog.model.User;
import ecom_blog.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordChangeController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordChangeController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Authentication authentication, Model model) {
        if (authentication == null)
            return "redirect:/login";
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String handleChangePassword(
            Authentication authentication,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        if (authentication == null)
            return "redirect:/login";

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            return "auth/change-password";
        }

        if (newPassword.length() < 6) {
            model.addAttribute("error", "Le mot de passe doit faire au moins 6 caractères");
            return "auth/change-password";
        }

        User user = userService.findByEmail(authentication.getName());
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        // On ne passe pas par saveUser car il ré-encoderais le mot de passe déjà encodé
        // Mais ici on veut juste mettre à jour le password.
        // On va plutôt appeler un repository direct ou une méthode de service dédiée si
        // elle existe.
        // Voyons UserService.

        // Comme saveUser dans UserServiceImpl appelle encode, on va plutôt faire une
        // méthode dédiée ou utiliser le repo.
        // Pour faire simple j'utilise saveUser mais je dois faire attention.
        // En fait UserServiceImpl.saveUser encode TOUJOURS.
        // Donc j'envoie le mot de passe en CLAIR à saveUser.

        user.setPassword(newPassword);
        userService.saveUser(user);

        return "redirect:/login?passwordChanged";
    }
}
