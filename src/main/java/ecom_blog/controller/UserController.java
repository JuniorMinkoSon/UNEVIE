package ecom_blog.controller;

import ecom_blog.security.CustomUserDetails;
import ecom_blog.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CommandeService commandeService;

    @GetMapping("/dashboard")
    public String dashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (userDetails != null && userDetails.getUser() != null) {
            model.addAttribute("commandes", commandeService.getByUser(userDetails.getUser()));
        }
        return "user/dashboard";
    }
}
