package ecom_blog.service;

import ecom_blog.model.Categorie;
import ecom_blog.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    // 🔹 Lister toutes les catégories
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    // 🔹 Sauvegarder une catégorie
    public Categorie save(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    // 🔹 Trouver par ID
    public Categorie findById(Long id) {
        return categorieRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));
    }

    // 🔹 Supprimer
    public void delete(Long id) {
        categorieRepository.deleteById(Math.toIntExact(id));
    }
}
