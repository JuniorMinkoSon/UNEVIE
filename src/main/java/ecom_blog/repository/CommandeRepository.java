package ecom_blog.repository;

import ecom_blog.model.Commande;
import ecom_blog.model.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // Compte le nombre total de commandes
    long count();

    // Compte les commandes par statut
    long countByStatut(String statut);

    // Récupère les 5 dernières commandes
    @Query("SELECT c FROM Commande c ORDER BY c.dateCommande DESC LIMIT 5")
    List<Commande> findTop5ByOrderByDateCommandeDesc();

    // Récupère les commandes d'un utilisateur spécifique
    List<Commande> findByUserOrderByDateCommandeDesc(ecom_blog.model.User user);

    // Trouver les commandes validées mais SANS prestataire (Offres disponibles)
    List<Commande> findByStatutAndPrestataireIsNull(String statut);

    // Trouver TOUTES les commandes sans prestataire (Debug/Fallback)
    List<Commande> findByPrestataireIsNull();

    // Trouver la mission en cours d'un prestataire (ex: pas encore LIVRÉ)
    // On suppose qu'un prestataire ne peut avoir qu'une mission active à la fois
    // pour simplifier
    List<Commande> findByPrestataireAndStatutNot(Prestataire prestataire, String statutExclu);

    // Trouver l'historique des missions d'un prestataire par statut (ex: LIVREE)
    List<Commande> findByPrestataireAndStatut(Prestataire prestataire, String statut);

    @Query(value = "SELECT EXTRACT(MONTH FROM c.date_commande) as month, COUNT(*) as count " +
            "FROM commandes c " +
            "WHERE EXTRACT(YEAR FROM c.date_commande) = :year " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> countOrdersByMonth(@Param("year") int year);
}
