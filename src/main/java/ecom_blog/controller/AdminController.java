package ecom_blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // ✅ Page Dashboard Admin
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Tu peux envoyer des données si nécessaire
        // Ex : model.addAttribute("stats", someService.getDashboardStats());
        return "admin/dashboard"; // ✅ Correspond exactement à templates/admin/dashboard.html
    }
}
