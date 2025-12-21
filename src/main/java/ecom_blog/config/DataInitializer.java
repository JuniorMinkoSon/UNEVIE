package ecom_blog.config;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("=====================================");
        log.info("DÉMARRAGE DE L'INITIALISATION");
        log.info("=====================================");

        try {
            log.info("UserRepository injecté: {}", userRepository != null);
            log.info("PasswordEncoder injecté: {}", passwordEncoder != null);

            long userCount = userRepository.count();
            log.info("Nombre d'utilisateurs existants: {}", userCount);

            String adminEmail = "admin@barikaweb.com";
            log.info("Recherche de l'admin avec email: {}", adminEmail);

            User existingAdmin = userRepository.findByEmail(adminEmail);

            if (existingAdmin != null) {
                log.info("L'administrateur existe déjà!");
                log.info("   - ID: {}", existingAdmin.getId());
                log.info("   - Nom: {}", existingAdmin.getNom());
                log.info("   - Email: {}", existingAdmin.getEmail());
                log.info("   - Role: {}", existingAdmin.getRole());
            } else {
                log.info("Aucun admin trouvé, création en cours...");

                User admin = new User();
                admin.setNom("Administrateur");
                admin.setEmail(adminEmail);

                String rawPassword = "admin123";
                String encodedPassword = passwordEncoder.encode(rawPassword);
                log.info("Mot de passe encodé: {}", encodedPassword.substring(0, 20) + "...");

                admin.setPassword(encodedPassword);
                admin.setRole(Role.ROLE_ADMIN);
                admin.setTelephone("0000000000");

                log.info("Sauvegarde de l'admin...");
                User savedAdmin = userRepository.save(admin);

            }

            userCount = userRepository.count();
            log.info(" Nombre total d'utilisateurs après initialisation: {}", userCount);

            log.info("Liste de tous les utilisateurs:");
            userRepository.findAll().forEach(user -> {
                log.info("   - ID: {}, Email: {}, Role: {}",
                        user.getId(), user.getEmail(), user.getRole());
            });

        } catch (Exception e) {
            log.error("ERREUR CRITIQUE lors de l'initialisation!", e);
            log.error("Message: {}", e.getMessage());
            log.error("Type: {}", e.getClass().getName());
            e.printStackTrace();
        }

        log.info("=====================================");
        log.info("✓ FIN DE L'INITIALISATION");
        log.info("=====================================");
    }
}