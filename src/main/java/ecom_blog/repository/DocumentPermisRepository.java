package ecom_blog.repository;

import ecom_blog.model.DocumentPermis;
import ecom_blog.model.enums.StatutPermis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentPermisRepository extends JpaRepository<DocumentPermis, Long> {

    // Trouver par commande
    Optional<DocumentPermis> findByCommande_Id(Long commandeId);

    // Trouver par statut
    List<DocumentPermis> findByStatut(StatutPermis statut);

    // Tous les permis en attente de vérification
    List<DocumentPermis> findByStatutOrderByDateUploadAsc(StatutPermis statut);
}
