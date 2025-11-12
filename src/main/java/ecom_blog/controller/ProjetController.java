package ecom_blog.controller;

import ecom_blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjetController {

    @Autowired
    private ArticleService articleService;

    // ✅ Affiche la page des projets réalisés
    //@GetMapping("/projets")
    public String showProjets(Model model) {
        model.addAttribute("articles", articleService.getAll());
        return "projets"; // => templates/projets.html
    }
}
