package ecom_blog.repository;

import ecom_blog.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    /**
     * Trouve la route active pour une commande
     */
    Optional<Route> findByCommandeIdAndRouteActiveTrue(Long commandeId);

    /**
     * Trouve toutes les routes actives
     */
    List<Route> findByRouteActiveTrue();

    /**
     * Trouve les routes pour un véhicule
     */
    @Query("SELECT r FROM Route r WHERE r.vehiculeId = :vehiculeId ORDER BY r.created_at DESC")
    List<Route> findByVehiculeIdOrderByCreated_atDesc(@Param("vehiculeId") Long vehiculeId);

    /**
     * Trouve les routes pour une commande
     */
    @Query("SELECT r FROM Route r WHERE r.commandeId = :commandeId ORDER BY r.created_at DESC")
    List<Route> findByCommandeIdOrderByCreated_atDesc(@Param("commandeId") Long commandeId);
}
