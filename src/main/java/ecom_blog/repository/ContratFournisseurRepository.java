package ecom_blog.repository;

import ecom_blog.model.ContratFournisseur;
import ecom_blog.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContratFournisseurRepository extends JpaRepository<ContratFournisseur, Long> {

    Optional<ContratFournisseur> findByFournisseur(Fournisseur fournisseur);

    Optional<ContratFournisseur> findByFournisseurId(Long fournisseurId);

    boolean existsByFournisseurId(Long fournisseurId);
}
