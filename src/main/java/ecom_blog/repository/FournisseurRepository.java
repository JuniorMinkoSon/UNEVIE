package ecom_blog.repository;

import ecom_blog.model.Fournisseur;
import ecom_blog.model.Secteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    List<Fournisseur> findBySecteur(Secteur secteur);

    List<Fournisseur> findByActifTrue();

    long countByActifTrue();

    List<Fournisseur> findBySecteurAndActifTrue(Secteur secteur);

    List<Fournisseur> findByContratAccepte(boolean accepte);

    List<Fournisseur> findByNomEntrepriseContainingIgnoreCase(String nom);

    Optional<Fournisseur> findByUserId(Long userId);

    @Query("SELECT f FROM Fournisseur f WHERE f.actif = true ORDER BY f.chiffreAffaires DESC")
    List<Fournisseur> findTopFournisseurs();

    @Query("SELECT f FROM Fournisseur f WHERE f.secteur = :secteur AND f.actif = true ORDER BY f.nombreReservations DESC")
    List<Fournisseur> findPopularBySecteur(Secteur secteur);

    @Query("SELECT COUNT(f) FROM Fournisseur f WHERE f.secteur = :secteur")
    long countBySecteur(Secteur secteur);
}
