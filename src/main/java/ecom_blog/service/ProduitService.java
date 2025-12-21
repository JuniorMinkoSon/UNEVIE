package ecom_blog.service;

import ecom_blog.dto.UpdateProduitDto;
import ecom_blog.model.Produit;
import ecom_blog.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    // 📁 Dossier de stockage des images produits
    private final String uploadDir = "src/main/resources/static/uploads/";

    // 💾 Enregistrer un produit avec image
    public void save(Produit produit, MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(uploadDir + image.getOriginalFilename());
                Files.write(path, bytes);
                produit.setImageUrl(image.getOriginalFilename());
            }
            produitRepository.save(produit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 📜 Obtenir tous les produits
    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    // ✅ Obtenir uniquement les produits disponibles
    public List<Produit> getAllDisponibles() {
        return produitRepository.findAll()
                .stream()
                .filter(Produit::isDisponible)
                .collect(Collectors.toList());
    }

    // 🔍 Récupérer un produit par ID
    public Produit findById(Long id) {
        Optional<Produit> optionalProduit = produitRepository.findById(id);
        return optionalProduit.orElse(null);
    }

    // ✅ Alias pour compatibilité avec ton contrôleur
    public Produit getById(Long id) {
        return findById(id);
    }

    // 🗑️ Supprimer un produit
    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    // 📝 Mettre à jour un produit
    public void update(Long id, UpdateProduitDto dto, MultipartFile image) {
        Produit produit = getById(id);
        if (produit != null) {
            produit.setNom(dto.getNom());
            produit.setCategorie(dto.getCategorie());
            produit.setPrix(dto.getPrix());
            produit.setDescription(dto.getDescription());
            produit.setDisponible(dto.isDisponible());

            if (image != null && !image.isEmpty()) {
                try {
                    byte[] bytes = image.getBytes();
                    Path path = Paths.get(uploadDir + image.getOriginalFilename());
                    Files.write(path, bytes);
                    produit.setImageUrl(image.getOriginalFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            produitRepository.save(produit);
        }
    }

    // 🔢 Compter le nombre total de produits
    public long count() {
        return produitRepository.count();
    }

    // 🔍 Produits par catégorie (disponibles uniquement)
    public List<Produit> getByCategorie(String categorie) {
        return produitRepository.findByCategorieIgnoreCaseAndDisponibleTrue(categorie);
    }

    // 📋 Obtenir toutes les catégories distinctes
    public List<String> getAllCategories() {
        return produitRepository.findAll()
                .stream()
                .map(Produit::getCategorie)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

}
