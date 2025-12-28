package ecom_blog.controller;

import ecom_blog.dto.EntrepriseDto;
import ecom_blog.model.enums.CategorieEntreprise;
import ecom_blog.service.EntrepriseService;
import ecom_blog.model.Produit;
import ecom_blog.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
@RequiredArgsConstructor
public class EntrepriseController {

    private final EntrepriseService entrepriseService;
    private final ProduitRepository produitRepository;

    @GetMapping
    public ResponseEntity<List<EntrepriseDto>> getEntreprises(
            @RequestParam(required = false) CategorieEntreprise categorie,
            @RequestParam(required = false) Long quartier) {
        List<EntrepriseDto> entreprises = entrepriseService.getEntreprisesByFilters(categorie, quartier);
        return ResponseEntity.ok(entreprises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrepriseDto> getEntreprise(@PathVariable Long id) {
        return ResponseEntity.ok(entrepriseService.getEntrepriseById(id));
    }

    @GetMapping("/{id}/produits")
    public ResponseEntity<List<Produit>> getProduitsEntreprise(@PathVariable Long id) {
        List<Produit> produits = produitRepository.findByEntreprise_IdAndDisponibleTrue(id);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<EntrepriseDto>> getActiveEntreprises() {
        return ResponseEntity.ok(entrepriseService.getActiveEntreprises());
    }
}
