package ecom_blog.controller.admin;

import ecom_blog.model.Produit;
import ecom_blog.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/produits")
public class AdminProduitController {

    private final ProduitService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("produits", service.getAll());
        return "admin/produits";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("produit", new Produit());
        return "admin/add-product";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute Produit produit,
                       @RequestParam("image") MultipartFile image) {

        service.save(produit, image);
        return "redirect:/admin/produits";
    }
}
