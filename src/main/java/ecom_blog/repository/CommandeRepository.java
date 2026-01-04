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
}
