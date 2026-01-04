package ecom_blog.controller;

import ecom_blog.model.Categorie;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CategorieService;
import ecom_blog.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private ProduitService produitService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategorieService categorieService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("produits", produitService.getAll());
        model.addAttribute("articles", articleService.getAll());
        return "user/index";
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("articles", articleService.getAll());
        return "user/blog";
    }

    // 🏷️ Articles par catégorie (public)
    @GetMapping("/blog/categorie/{id}")
    public String articlesByCategory(@PathVariable Long id, Model model) {
        model.addAttribute("articles", articleService.findByCategory(id)); // Retourne des DTOs
        model.addAttribute("categories", categorieService.findAll());

        // Récupérer la catégorie actuelle pour l'afficher
        Categorie currentCategory = categorieService.findById(id);
        model.addAttribute("currentCategory", currentCategory);

        return "user/articles-par-categorie";
    }

    // 📖 Détail d'un article spécifique
    @GetMapping("/blog/article/{id}")
    public String articleDetail(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "user/article-details";
    }
    //
    // @GetMapping("/produits")
    // public String produits(Model model) {
    // model.addAttribute("produits", produitService.getAll());
    // return "user/product-list";
    // }

    @GetMapping("/contact")
    public String contact() {
        return "user/contact";
    }

    @GetMapping("/confirmation")
    public String confirmation() {
        return "user/confirmation";
    }

    @GetMapping("/services")
    public String services() {
        return "user/services";
    }

    @GetMapping("/propos")
    public String propos() {
        return "user/propos";
    }

    // @GetMapping("/projets")
    public String projets(Model model) {
        model.addAttribute("produits", produitService.getAllDisponibles());
        return "user/projets";
    }

    @GetMapping("/objectifs")
    public String objectifs() {
        return "user/objectifs";
    }

    @GetMapping("/faq")
    public String faq() {
        return "user/faq";
    }

    @GetMapping("/mentions-legales")
    public String mentionsLegales() {
        return "user/mentions-legales";
    }

    @GetMapping("/politique")
    public String politique() {
        return "user/politique";
    }

}
