package ecom_blog.repository;

import ecom_blog.model.Fournisseur;
import ecom_blog.model.Secteur;
import ecom_blog.model.ServiceFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceFournisseurRepository extends JpaRepository<ServiceFournisseur, Long> {

    List<ServiceFournisseur> findBySecteur(Secteur secteur);

    List<ServiceFournisseur> findByFournisseur(Fournisseur fournisseur);

    List<ServiceFournisseur> findByFournisseurId(Long fournisseurId);

    List<ServiceFournisseur> findByFournisseurAndDisponibleTrue(Fournisseur fournisseur);

    List<ServiceFournisseur> findBySecteurAndDisponibleTrue(Secteur secteur);

    List<ServiceFournisseur> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT s FROM ServiceFournisseur s WHERE s.disponible = true ORDER BY s.nombreReservations DESC")
    List<ServiceFournisseur> findMostPopular();

    @Query("SELECT s FROM ServiceFournisseur s WHERE s.secteur = :secteur AND s.disponible = true ORDER BY s.nombreReservations DESC")
    List<ServiceFournisseur> findMostPopularBySecteur(Secteur secteur);

    @Query("SELECT s FROM ServiceFournisseur s WHERE s.prix BETWEEN :min AND :max AND s.disponible = true")
    List<ServiceFournisseur> findByPrixBetween(Double min, Double max);

    @Query("SELECT COUNT(s) FROM ServiceFournisseur s WHERE s.fournisseur.id = :fournisseurId")
    long countByFournisseurId(Long fournisseurId);
}
