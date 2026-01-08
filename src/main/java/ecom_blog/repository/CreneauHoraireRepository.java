package ecom_blog.repository;

import ecom_blog.model.CreneauHoraire;
import ecom_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CreneauHoraireRepository extends JpaRepository<CreneauHoraire, Long> {

    /**
     * Trouve tous les créneaux disponibles d'un fournisseur après une date donnée
     */
    List<CreneauHoraire> findByFournisseurAndDisponibleTrueAndDebutAfter(
            User fournisseur, LocalDateTime maintenant);

    /**
     * Trouve un créneau spécifique par fournisseur et plage horaire
     */
    Optional<CreneauHoraire> findByFournisseurAndDebutAndFin(
            User fournisseur, LocalDateTime debut, LocalDateTime fin);

    /**
     * Trouve tous les créneaux d'un fournisseur
     */
    List<CreneauHoraire> findByFournisseurOrderByDebutAsc(User fournisseur);

    /**
     * Trouve les créneaux récurrents d'un fournisseur
     */
    List<CreneauHoraire> findByFournisseurAndRecurrentTrue(User fournisseur);
}
