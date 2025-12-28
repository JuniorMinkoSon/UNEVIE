package ecom_blog.repository;

import ecom_blog.model.EquipeFiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeFiestaRepository extends JpaRepository<EquipeFiesta, Long> {

    // Trouver les équipes créées par un utilisateur
    List<EquipeFiesta> findByCreateur_Id(Long userId);

    // Trouver les équipes dont l'utilisateur est membre
    @Query("SELECT e FROM EquipeFiesta e JOIN e.membres m WHERE m.id = :userId")
    List<EquipeFiesta> findByMembre(@Param("userId") Long userId);

    // Toutes les équipes d'un utilisateur (créateur OU membre)
    @Query("SELECT DISTINCT e FROM EquipeFiesta e " +
            "WHERE e.createur.id = :userId " +
            "OR :userId IN (SELECT m.id FROM e.membres m)")
    List<EquipeFiesta> findAllByUser(@Param("userId") Long userId);
}
