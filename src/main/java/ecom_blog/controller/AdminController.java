package ecom_blog.controller;

import ecom_blog.dto.CreateArticleDto;
import ecom_blog.dto.CreateProduitDto;
import ecom_blog.dto.UpdateArticleDto;
import ecom_blog.dto.UpdateProduitDto;
import ecom_blog.mapper.ArticleMapper;
import ecom_blog.mapper.ProduitMapper;
import ecom_blog.model.Article;
import ecom_blog.model.Produit;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import ecom_blog.service.ReservationService;
import ecom_blog.service.StatistiquesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;

import java.util.List;
import ecom_blog.service.CategorieService;
import ecom_blog.service.UserService;

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
    private CategorieService categorieService;

    @Autowired
    private ProduitMapper produitMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ecom_blog.service.FournisseurService fournisseurService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatistiquesService statistiquesService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", statistiquesService.getGlobalKpis());
        model.addAttribute("recentCommandes", commandeService.findLast5());
        model.addAttribute("orderStats", commandeService.getMonthlyOrders());

        // Stats financières UneVie
        var financialSummary = statistiquesService.getFinancialSummary();
        model.addAttribute("totalCommissions", financialSummary.get("totalCommissions"));
        model.addAttribute("totalRevenus", financialSummary.get("revenueServices")); // Pour garder la compatibilité
                                                                                     // avec le template existant
                                                                                     // (Services only for now on
                                                                                     // dashboard)

        return "admin/dashboard";
    }

    @GetMapping("/statistiques")
    public String statistiques(Model model) {
        java.time.LocalDate now = java.time.LocalDate.now();

        model.addAttribute("totalCommissions", reservationService.getTotalCommissions());
        model.addAttribute("totalRevenus", reservationService.getTotalRevenus());
        model.addAttribute("statsMensuelles", reservationService.getStatistiquesMensuelles(now.getYear()));
        model.addAttribute("statsFournisseurs", reservationService.getStatistiquesParFournisseur());
        model.addAttribute("fournisseursActifs", fournisseurService.findActifs());

        return "admin/statistiques";
    }

    @GetMapping("/fournisseurs")
    public String listFournisseurs(Model model) {
        model.addAttribute("fournisseurs", fournisseurService.findAll());
        model.addAttribute("newFournisseur", new ecom_blog.dto.AdminCreateFournisseurDto());
        model.addAttribute("secteurs", ecom_blog.model.Secteur.values());
        model.addAttribute("openModal", false);
        model.addAttribute("isEdit", false);
        return "admin/fournisseurs-list";
    }

    @PostMapping("/fournisseurs/save")
    public String saveFournisseur(@Valid @ModelAttribute("newFournisseur") ecom_blog.dto.AdminCreateFournisseurDto dto,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fournisseurs", fournisseurService.findAll());
            model.addAttribute("secteurs", ecom_blog.model.Secteur.values());
            model.addAttribute("openModal", true);
            return "admin/fournisseurs-list";
        }

        // 1. Créer l'utilisateur
        ecom_blog.model.User user = new ecom_blog.model.User();
        user.setEmail(dto.getEmail());
        user.setNom(dto.getNom());
        user.setTelephone(dto.getTelephone());
        user.setRole(ecom_blog.model.Role.ROLE_FOURNISSEUR);
        user.setPassword("UNEVIE-2024"); // Mot de passe par défaut
        user.setMustChangePassword(true); // Forcer le changement

        java.util.Optional<ecom_blog.model.User> existing = userService.findByEmailOptional(dto.getEmail());
        if (existing.isPresent()) {
            bindingResult.rejectValue("email", "error.user", "Cet email est déjà utilisé");
            model.addAttribute("fournisseurs", fournisseurService.findAll());
            model.addAttribute("secteurs", ecom_blog.model.Secteur.values());
            model.addAttribute("openModal", true);
            model.addAttribute("isEdit", false);
            return "admin/fournisseurs-list";
        }

        user = userService.saveUser(user);

        // 2. Créer le fournisseur
        fournisseurService.inscrireFournisseur(
                user,
                dto.getNomEntreprise(),
                dto.getSecteur(),
                dto.getDescription(),
                dto.getAdresse(),
                dto.getVille(),
                dto.getTelephone());

        return "redirect:/admin/fournisseurs?success";
    }

    @GetMapping("/fournisseurs/{id}")
    @ResponseBody
    public ecom_blog.dto.AdminCreateFournisseurDto getFournisseur(@PathVariable Long id) {
        ecom_blog.model.Fournisseur f = fournisseurService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        ecom_blog.dto.AdminCreateFournisseurDto dto = new ecom_blog.dto.AdminCreateFournisseurDto();
        dto.setId(f.getId());
        dto.setEmail(f.getUser().getEmail());
        dto.setNom(f.getUser().getNom());
        dto.setNomEntreprise(f.getNomEntreprise());
        dto.setSecteur(f.getSecteur());
        dto.setDescription(f.getDescription());
        dto.setAdresse(f.getAdresse());
        dto.setVille(f.getVille());
        dto.setTelephone(f.getTelephone());
        return dto;
    }

    @PostMapping("/fournisseurs/update")
    public String updateFournisseur(
            @Valid @ModelAttribute("newFournisseur") ecom_blog.dto.AdminCreateFournisseurDto dto,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("fournisseurs", fournisseurService.findAll());
            model.addAttribute("secteurs", ecom_blog.model.Secteur.values());
            model.addAttribute("openModal", true);
            return "admin/fournisseurs-list";
        }

        fournisseurService.updateFournisseur(dto);
        return "redirect:/admin/fournisseurs?updated";
    }

    @GetMapping("/product/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("produit", new CreateProduitDto());
        model.addAttribute("categories", categorieService.findAll());
        return "admin/add-product";
    }

    @PostMapping("/product/save")
    public String saveProduct(@Valid @ModelAttribute("produit") CreateProduitDto dto, BindingResult bindingResult,
            Model model, @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categorieService.findAll());
            return "admin/add-product";
        }

        Produit produit = produitMapper.toEntity(dto);
        produitService.save(produit, images);
        return "redirect:/admin/products?success=produit";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        try {
            produitService.delete(id);
            return "redirect:/admin/products?deleted";
        } catch (Exception e) {
            return "redirect:/admin/products?error=delete_constraint";
        }
    }

    @GetMapping("/product/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Produit produit = produitService.getById(id);
        if (produit == null) {
            return "redirect:/admin/products?error=notfound";
        }

        UpdateProduitDto dto = new UpdateProduitDto();
        dto.setId(produit.getId());
        dto.setNom(produit.getNom());
        dto.setCategorie(produit.getCategorie());
        dto.setPrix(produit.getPrix());
        dto.setDescription(produit.getDescription());
        dto.setImageUrl(produit.getImageUrl());
        dto.setImageUrls(new java.util.ArrayList<>(produit.getImageUrls()));
        dto.setDisponible(produit.isDisponible());

        model.addAttribute("produit", dto);
        model.addAttribute("categories", categorieService.findAll());
        return "admin/edit-product";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Long id,
            @Valid @ModelAttribute("produit") UpdateProduitDto dto,
            BindingResult bindingResult,
            Model model,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categorieService.findAll());
            return "admin/edit-product";
        }

        produitService.update(id, dto, images);
        return "redirect:/admin/products?success=update";
    }

    @GetMapping("/article/add")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new CreateArticleDto());
        model.addAttribute("categories", categorieService.findAll());
        return "admin/add-article";
    }

    @PostMapping("/article/save")
    public String saveArticle(@Valid @ModelAttribute("article") CreateArticleDto dto, BindingResult bindingResult,
            Model model, @RequestParam(value = "images", required = false) java.util.List<MultipartFile> images) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categorieService.findAll());
            return "admin/add-article";
        }

        Article article = articleMapper.toEntity(dto);
        articleService.save(article, images);
        return "redirect:/admin/dashboard?success=article";
    }

    @GetMapping("/article/list")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleService.getAll());
        return "admin/articles-list";
    }

    @GetMapping("/article/edit/{id}")
    public String showEditArticleForm(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        model.addAttribute("categories", categorieService.findAll());
        return "admin/edit-article";
    }

    @PostMapping("/article/update/{id}")
    public String updateArticle(@PathVariable Long id,
            @Valid @ModelAttribute("article") UpdateArticleDto dto,
            BindingResult bindingResult,
            Model model,
            @RequestParam(value = "images", required = false) java.util.List<MultipartFile> images) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categorieService.findAll());

            dto.setId(id);
            try {
                var existingArticle = articleService.findById(id);
                dto.setImageUrl(existingArticle.getImageUrl());
            } catch (Exception e) {
            }

            return "admin/edit-article";
        }

        articleService.update(id, dto, images);
        return "redirect:/admin/article/list?success=update";
    }

    @GetMapping("/article/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return "redirect:/admin/article/list?success=delete";
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("produits", produitService.getAll());
        return "admin/products-list";
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("categorie", new ecom_blog.model.Categorie());
        return "admin/categories";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute("categorie") ecom_blog.model.Categorie categorie) {
        categorieService.save(categorie);
        return "redirect:/admin/categories?success";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categorieService.delete(id);
        return "redirect:/admin/categories?deleted";
    }

    /* ================== TRACKING GLOBAL ================== */

    @GetMapping("/tracking")
    public String trackingGlobal(Model model) {
        // Commandes en cours de livraison
        List<ecom_blog.model.Commande> commandesEnCours = commandeService
                .getCommandesEnCoursLivraison();

        // Livreurs actifs (ceux qui ont une position récente)
        // TODO: Créer une méthode findLivreursActifs dans UserService
        // Pour l'instant, on prend les livreurs des commandes en cours
        java.util.Set<ecom_blog.model.User> livreursActifs = new java.util.HashSet<>();
        for (ecom_blog.model.Commande c : commandesEnCours) {
            if (c.getLivreur() != null) {
                livreursActifs.add(c.getLivreur());
            }
        }

        model.addAttribute("commandesEnCours", commandesEnCours);
        model.addAttribute("livreursActifs", livreursActifs);

        return "admin/tracking-global";
    }
}
