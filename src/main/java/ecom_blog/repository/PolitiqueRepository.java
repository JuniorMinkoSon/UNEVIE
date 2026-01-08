package ecom_blog.repository;

import ecom_blog.model.Politique;
import ecom_blog.model.Secteur;
import ecom_blog.model.ServiceFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolitiqueRepository extends JpaRepository<Politique, Long> {

    /**
     * Trouve les politiques actives d'un secteur
     */
    List<Politique> findBySecteurAndActifTrue(Secteur secteur);

    /**
     * Trouve la politique active d'un service spécifique
     */
    Optional<Politique> findByServiceAndActifTrue(ServiceFournisseur service);

    /**
     * Trouve toutes les politiques actives
     */
    List<Politique> findByActifTrue();

    /**
     * Trouve les politiques par type
     */
    List<Politique> findByTypeAndActifTrue(ecom_blog.model.TypePolitique type);
}
