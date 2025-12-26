package ecom_blog.repository;

import ecom_blog.model.Prestataire;
import ecom_blog.model.User;
import ecom_blog.model.enums.TypePrestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire, Long> {

    // Trouver tous les prestataires disponibles
    List<Prestataire> findByDisponibleTrue();

    // Trouver les prestataires disponibles d'un type spécifique
    List<Prestataire> findByDisponibleTrueAndTypePrestataire(TypePrestataire typePrestataire);

    // Trouver les prestataires en service
    List<Prestataire> findByEnServiceTrue();

    // Trouver par type
    List<Prestataire> findByTypePrestataire(TypePrestataire typePrestataire);

    // Trouver par utilisateur associé
    Optional<Prestataire> findByUser(User user);
}
