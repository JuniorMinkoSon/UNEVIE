package ecom_blog.repository;

import ecom_blog.model.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {

    List<Quartier> findByCommune(String commune);

    Quartier findByNom(String nom);
}
