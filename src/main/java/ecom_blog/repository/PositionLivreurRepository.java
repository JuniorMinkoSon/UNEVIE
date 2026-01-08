package ecom_blog.repository;

import ecom_blog.model.Commande;
import ecom_blog.model.PositionLivreur;
import ecom_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionLivreurRepository extends JpaRepository<PositionLivreur, Long> {

    /**
     * Trouve l'historique des positions d'une commande (ordre décroissant)
     */
    List<PositionLivreur> findByCommandeOrderByTimestampDesc(Commande commande);

    /**
     * Trouve la dernière position d'un livreur
     */
    Optional<PositionLivreur> findFirstByLivreurOrderByTimestampDesc(User livreur);

    /**
     * Trouve toutes les positions d'un livreur
     */
    List<PositionLivreur> findByLivreurOrderByTimestampDesc(User livreur);

    /**
     * Trouve les positions d'une commande (ordre croissant pour trajet)
     */
    List<PositionLivreur> findByCommandeOrderByTimestampAsc(Commande commande);
}
