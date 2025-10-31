package ecom_blog.controller;

import ecom_blog.model.Article;
import ecom_blog.model.Produit;
import ecom_blog.service.ArticleService;
import ecom_blog.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private ProduitService produitService;
    @Autowired private ArticleService articleService;

    // --- Tableau de bord ---
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("produits", produitService.getAll().size());
        stats.put("articles", articleService.getAll().size());
        stats.put("commandes", 0); // à compléter plus tard
        model.addAttribute("stats", stats);
        return "admin/dashboard";
    }

    // --- FORMULAIRE D’AJOUT DE PRODUIT ---
    @GetMapping("/product/add")
    public String addProductForm(Model model) {
        model.addAttribute("produit", new Produit());
        return "admin/add-product";
    }

    // --- SAUVEGARDE DU PRODUIT ---
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Produit produit) {
        produitService.save(produit);
        return "redirect:/admin/dashboard";
    }

    // --- FORMULAIRE D’AJOUT D’ARTICLE ---
    @GetMapping("/article/add")
    public String addArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/add-article";
    }

    // --- SAUVEGARDE D’ARTICLE ---
    @PostMapping("/article/save")
    public String saveArticle(@ModelAttribute Article article) {
        articleService.save(article);
        return "redirect:/admin/dashboard";
    }
}
