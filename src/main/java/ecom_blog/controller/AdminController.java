package ecom_blog.controller;

import ecom_blog.model.Article;
import ecom_blog.model.Produit;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import ecom_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private UserService userService;

    // 🏠 Tableau de bord principal
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ✅ Statistiques globales (ordre logique et cohérent)
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("👥 Utilisateurs", userService.count());
        stats.put("🛍️ Produits", produitService.count());
        stats.put("📰 Articles", articleService.count());
        stats.put("📦 Commandes", commandeService.count());
        stats.put("🚚 Livrées", commandeService.countByStatut("LIVRÉ"));

        model.addAttribute("stats", stats);
        model.addAttribute("recentCommandes", commandeService.findLast5());

        return "admin/dashboard";
    }

    // ➕ Formulaire d’ajout de produit
    @GetMapping("/product/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("produit", new Produit());
        return "admin/add-product";
    }

    // 💾 Sauvegarde d’un produit
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Produit produit, @RequestParam("image") MultipartFile image) {
        produitService.save(produit, image);
        return "redirect:/admin/dashboard?success=produit";
    }

    // ➕ Formulaire d’ajout d’article
    @GetMapping("/article/add")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/add-article";
    }

    // 💾 Sauvegarde d’un article
    @PostMapping("/article/save")
    public String saveArticle(@ModelAttribute Article article, @RequestParam("image") MultipartFile image) {
        articleService.save(article, image);
        return "redirect:/admin/dashboard?success=article";
    }
}
