package ecom_blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class EntrepriseViewController {

    @GetMapping("/entreprises")
    public String entreprisesPage() {
        return "user/entreprises";
    }

    @GetMapping("/entreprise/{id}")
    public String entrepriseDetailsPage(@PathVariable Long id, Model model) {
        model.addAttribute("entrepriseId", id);
        return "user/entreprise-details";
    }
}
