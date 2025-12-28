package ecom_blog.repository;

import ecom_blog.model.CreneauDisponibilite;
import ecom_blog.model.enums.StatutCreneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreneauDisponibiliteRepository extends JpaRepository<CreneauDisponibilite, Long> {

    // Trouver tous les créneaux d'un produit
    List<CreneauDisponibilite> findByProduit_Id(Long produitId);

    // Trouver par produit et statut
    List<CreneauDisponibilite> findByProduit_IdAndStatut(Long produitId, StatutCreneau statut);

    // Créneaux disponibles dans une période
    @Query("SELECT c FROM CreneauDisponibilite c WHERE c.produit.id = :produitId " +
            "AND c.statut = 'DISPONIBLE' " +
            "AND c.dateDebut >= :debut " +
            "AND c.dateFin <= :fin " +
            "ORDER BY c.dateDebut ASC")
    List<CreneauDisponibilite> findAvailableSlots(
            @Param("produitId") Long produitId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);

    // Vérifier s'il y a conflit avec une période donnée
    @Query("SELECT c FROM CreneauDisponibilite c WHERE c.produit.id = :produitId " +
            "AND c.statut != 'DISPONIBLE' " +
            "AND ((c.dateDebut <= :fin AND c.dateFin >= :debut))")
    List<CreneauDisponibilite> findConflictingSlots(
            @Param("produitId") Long produitId,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);
}
