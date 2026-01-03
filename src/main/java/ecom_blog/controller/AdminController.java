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
import ecom_blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ecom_blog.service.CategorieService;

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
    @Autowired
    private CategorieService categorieService;

    @Autowired
    private ProduitMapper produitMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("👥 Utilisateurs", userService.count());
        stats.put("🛍️ Produits", produitService.count());
        stats.put("📰 Articles", articleService.count());
        stats.put("📦 Commandes", commandeService.count());
        stats.put("🚚 Livrées", commandeService.countByStatut("LIVRÉ"));

        model.addAttribute("stats", stats);
        model.addAttribute("recentCommandes", commandeService.findLast5());
        model.addAttribute("orderStats", commandeService.getMonthlyOrders());

        return "admin/dashboard";
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
}
