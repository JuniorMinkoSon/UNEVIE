package ecom_blog.service;

import ecom_blog.model.Produit;
import ecom_blog.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class ProduitService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Autowired
    private ProduitRepository produitRepository;

    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    public long count() {
        return produitRepository.count();
    }

    // ✅ Corrigé : renvoie directement un Produit (plus de Optional)
    public Produit findById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    public void save(Produit produit, MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, image.getBytes());
                produit.setImageUrl("/uploads/" + fileName);
            }
            produitRepository.save(produit);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l’enregistrement de l’image", e);
        }
    }

    public boolean delete(Long id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
