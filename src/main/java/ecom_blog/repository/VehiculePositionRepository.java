package ecom_blog.repository;

import ecom_blog.model.VehiculePosition;
import ecom_blog.model.enums.StatutVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculePositionRepository extends JpaRepository<VehiculePosition, Long> {

    /**
     * Trouve la dernière position d'un véhicule
     */
    Optional<VehiculePosition> findFirstByVehiculeIdOrderByTimestampDesc(Long vehiculeId);

    /**
     * Trouve toutes les positions d'un véhicule dans une période
     */
    List<VehiculePosition> findByVehiculeIdAndTimestampBetweenOrderByTimestampAsc(
            Long vehiculeId,
            LocalDateTime debut,
            LocalDateTime fin);

    /**
     * Trouve tous les véhicules par statut
     */
    @Query("SELECT DISTINCT vp FROM VehiculePosition vp WHERE vp.id IN (" +
            "SELECT MAX(vp2.id) FROM VehiculePosition vp2 GROUP BY vp2.vehiculeId" +
            ") AND vp.statut = :statut")
    List<VehiculePosition> findLatestPositionsByStatut(@Param("statut") StatutVehicule statut);

    /**
     * Trouve tous les véhicules actifs (dernière position)
     */
    @Query("SELECT vp FROM VehiculePosition vp WHERE vp.id IN (" +
            "SELECT MAX(vp2.id) FROM VehiculePosition vp2 GROUP BY vp2.vehiculeId" +
            ")")
    List<VehiculePosition> findLatestPositionsForAllVehicles();

    /**
     * Trouve les véhicules dans un rayon géographique
     */
    @Query(value = """
            SELECT vp.* FROM vehicule_positions vp
            INNER JOIN (
                SELECT vehicule_id, MAX(id) as max_id
                FROM vehicule_positions
                GROUP BY vehicule_id
            ) latest ON vp.id = latest.max_id
            WHERE (
                6371 * acos(
                    cos(radians(:latitude)) *
                    cos(radians(vp.latitude)) *
                    cos(radians(vp.longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) *
                    sin(radians(vp.latitude))
                )
            ) <= :radiusKm
            """, nativeQuery = true)
    List<VehiculePosition> findVehiclesNearby(@Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusKm") Double radiusKm);

    /**
     * Supprime les anciennes positions (nettoyage)
     */
    void deleteByTimestampBefore(LocalDateTime date);
}
