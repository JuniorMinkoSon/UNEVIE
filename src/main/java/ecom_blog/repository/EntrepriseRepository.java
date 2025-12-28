package ecom_blog.repository;

import ecom_blog.model.Entreprise;
import ecom_blog.model.enums.CategorieEntreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {

    // Filtrer par catégorie
    List<Entreprise> findByCategorie(CategorieEntreprise categorie);

    // Filtrer par quartier
    List<Entreprise> findByQuartier_Id(Long quartierId);

    // Filtrer par catégorie ET quartier
    List<Entreprise> findByCategorieAndQuartier_Id(CategorieEntreprise categorie, Long quartierId);

    // Seulement les entreprises avec contrat actif
    List<Entreprise> findByContratActifTrue();

    // Entreprises avec contrat actif ET catégorie
    List<Entreprise> findByContratActifTrueAndCategorie(CategorieEntreprise categorie);

    // Recherche par nom
    List<Entreprise> findByNomContainingIgnoreCase(String nom);
}
