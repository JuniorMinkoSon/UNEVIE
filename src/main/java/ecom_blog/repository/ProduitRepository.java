package ecom_blog.repository;

import ecom_blog.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByDisponibleTrue();

    // 🔥 FILTRAGE PAR CATÉGORIE (STRING)
    List<Produit> findByCategorieIgnoreCaseAndDisponibleTrue(String categorie);

    // 🏢 FILTRAGE PAR ENTREPRISE
    List<Produit> findByEntreprise_Id(Long entrepriseId);

    List<Produit> findByEntreprise_IdAndDisponibleTrue(Long entrepriseId);
}
