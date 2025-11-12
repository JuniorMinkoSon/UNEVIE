package ecom_blog.repository;

import ecom_blog.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // Compte le nombre total de commandes
    long count();

    // Compte les commandes par statut
    long countByStatut(String statut);

    // Récupère les 5 dernières commandes
    @Query("SELECT c FROM Commande c ORDER BY c.dateCommande DESC LIMIT 5")
    List<Commande> findTop5ByOrderByDateCommandeDesc();
}
