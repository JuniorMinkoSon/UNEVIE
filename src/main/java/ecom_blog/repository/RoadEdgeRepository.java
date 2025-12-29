package ecom_blog.repository;

import ecom_blog.model.RoadEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadEdgeRepository extends JpaRepository<RoadEdge, Long> {

    /**
     * Trouve toutes les arêtes partant d'un nœud donné
     */
    List<RoadEdge> findByNodeDepart_Id(Long nodeDepartId);

    /**
     * Trouve toutes les arêtes arrivant à un nœud donné
     */
    List<RoadEdge> findByNodeArrivee_Id(Long nodeArriveeId);

    /**
     * Trouve une arête entre deux nœuds spécifiques
     */
    @Query("SELECT e FROM RoadEdge e WHERE e.nodeDepart.id = :departId AND e.nodeArrivee.id = :arriveeId")
    List<RoadEdge> findEdgeBetweenNodes(@Param("departId") Long departId,
            @Param("arriveeId") Long arriveeId);

    /**
     * Trouve toutes les connexions bidirectionnelles
     */
    List<RoadEdge> findByBidirectionnelleTrue();

    /**
     * Trouve les arêtes par type de route
     */
    List<RoadEdge> findByTypeRoute(String typeRoute);
}
