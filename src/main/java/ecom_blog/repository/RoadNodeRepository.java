package ecom_blog.repository;

import ecom_blog.model.RoadNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoadNodeRepository extends JpaRepository<RoadNode, Long> {

    Optional<RoadNode> findByNom(String nom);

    List<RoadNode> findByTypeNode(String typeNode);

    /**
     * Trouve le nœud le plus proche d'une coordonnée GPS donnée
     * Utilise la formule de Haversine pour calculer la distance
     */
    @Query(value = """
            SELECT * FROM road_nodes
            ORDER BY (
                6371 * acos(
                    cos(radians(:latitude)) *
                    cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) *
                    sin(radians(latitude))
                )
            ) ASC
            LIMIT 1
            """, nativeQuery = true)
    Optional<RoadNode> findNearestNode(@Param("latitude") Double latitude,
            @Param("longitude") Double longitude);

    /**
     * Trouve tous les nœuds dans un rayon donné (en km)
     */
    @Query(value = """
            SELECT * FROM road_nodes
            WHERE (
                6371 * acos(
                    cos(radians(:latitude)) *
                    cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) *
                    sin(radians(latitude))
                )
            ) <= :radiusKm
            ORDER BY (
                6371 * acos(
                    cos(radians(:latitude)) *
                    cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) *
                    sin(radians(latitude))
                )
            )
            """, nativeQuery = true)
    List<RoadNode> findNodesWithinRadius(@Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radiusKm") Double radiusKm);
}
