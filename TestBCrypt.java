package ecom_blog;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123"; // 🔑 Ton mot de passe en clair
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Mot de passe : " + rawPassword);
        System.out.println("Hash BCrypt : " + encodedPassword);
    }
}
