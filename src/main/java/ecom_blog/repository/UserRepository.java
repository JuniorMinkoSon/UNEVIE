package ecom_blog.repository;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // 🔑 POUR L’ASSIGNATION LIVREUR
    List<User> findByRoleAndDisponible(Role role, boolean disponible);
}
