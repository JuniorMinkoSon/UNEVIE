package ecom_blog.controller;

import ecom_blog.model.Article;
import ecom_blog.model.Produit;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommandeService commandeService;

    // 🏠 Tableau de bord principal
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ✅ Affiche les statistiques de base
        model.addAttribute("stats", new Object() {
            public long articles = articleService.count();
            public long produits = produitService.count();
            public long commandes = commandeService.count();
        });
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
    public String saveProduct(@ModelAttribute Produit produit, MultipartFile image) {
        produitService.save(produit, image);
        return "redirect:/admin/dashboard";
    }

    // 📝 Formulaire d’ajout d’article
    @GetMapping("/article/add")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new Article());
        return "admin/add-article";
    }

    // 💾 Sauvegarde d’un article
    @PostMapping("/article/save")
    public String saveArticle(@ModelAttribute Article article, MultipartFile image) {
        articleService.save(article, image);
        return "redirect:/admin/dashboard";
    }

    // ⚠️ SUPPRIMÉ : méthode /admin/commandes
    // (elle est maintenant gérée dans AdminCommandeController)
}
