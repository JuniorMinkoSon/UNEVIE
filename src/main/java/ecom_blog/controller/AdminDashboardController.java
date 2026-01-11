package ecom_blog.controller.admin;

import ecom_blog.service.MessageContactService;
import ecom_blog.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminDashboardController {

    private final MessageContactService messageService;
    private final CommandeService commandeService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // ---- STATS ----
        Map<String, Long> stats = new HashMap<>();
        stats.put("messages", messageService.count());
        stats.put("commandes", commandeService.count());

        model.addAttribute("stats", stats);

        // ---- DERNIERS MESSAGES ----
        model.addAttribute("messages", messageService.getLast5());

        return "admin/dashboard";
    }
}
