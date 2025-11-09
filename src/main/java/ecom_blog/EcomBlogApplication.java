package ecom_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EcomBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcomBlogApplication.class, args);

        // ✅ Générer le hash pour ton mot de passe admin
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");  // Ton mot de passe ici
        System.out.println("\n=============================");
        System.out.println("🔐 Hash généré pour admin123 :");
        System.out.println(hash);
        System.out.println("=============================\n");
    }
}
