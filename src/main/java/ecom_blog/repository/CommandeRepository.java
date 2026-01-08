package ecom_blog.repository;

import ecom_blog.model.Commande;
import ecom_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    long countByStatut(String statut);

    List<Commande> findTop5ByOrderByDateCommandeDesc();

    List<Commande> findByUserOrderByDateCommandeDesc(User user);

    @Query("""
                SELECT MONTH(c.dateCommande), COUNT(c)
                FROM Commande c
                WHERE YEAR(c.dateCommande) = :year
                GROUP BY MONTH(c.dateCommande)
            """)
    List<Object[]> countOrdersByMonth(int year);

    @Query("""
                SELECT MONTH(c.dateCommande), SUM(c.total)
                FROM Commande c
                WHERE YEAR(c.dateCommande) = :year
                GROUP BY MONTH(c.dateCommande)
            """)
    List<Object[]> sumRevenueByMonth(int year);

    @Query("SELECT SUM(c.total) FROM Commande c")
    Double sumTotalRevenue();

    // ================== NOUVELLES REQUÊTES ==================

    /**
     * Trouve les commandes en attente non expirées
     */
    List<Commande> findByStatutAndExpireeFalse(String statut);

    /**
     * Trouve les commandes à évaluer (livrées depuis > délai, évaluation non
     * envoyée)
     */
    @Query("SELECT c FROM Commande c WHERE c.statut = 'LIVREE' " +
            "AND c.evaluationEnvoyee = false " +
            "AND c.dateExecution < :dateLimit")
    List<Commande> findCommandesAEvaluer(
            @org.springframework.data.repository.query.Param("dateLimit") java.time.LocalDateTime dateLimit);

    /**
     * Trouve les commandes par statut enum
     */
    List<Commande> findByStatutOrderByDateCommandeDesc(String statut);

    /**
     * Trouve les commandes d'un livreur
     */
    List<Commande> findByLivreurOrderByDateCommandeDesc(User livreur);

    /**
     * Trouve les commandes en cours de livraison
     */
    @Query("SELECT c FROM Commande c WHERE c.statut = 'EN_COURS_LIVRAISON'")
    List<Commande> findCommandesEnCoursLivraison();

    /**
     * Trouve les commandes d'un fournisseur spécifique
     */
    List<Commande> findByService_FournisseurIdAndStatutOrderByDateCommandeDesc(Long fournisseurId, String statut);

    long countByService_FournisseurIdAndStatut(Long fournisseurId, String statut);
}
