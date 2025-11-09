package ecom_blog.service;

import ecom_blog.model.User; // ✅ Ton entité
import ecom_blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1️⃣ Récupération de l'utilisateur dans la base
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + email);
        }

        // 2️⃣ Conversion du rôle enum → String
        String role = user.getRole().name(); // ex : "ROLE_ADMIN"

        // 3️⃣ Retirer "ROLE_" pour Spring Security (qui l'ajoute automatiquement)
        if (role.startsWith("ROLE_")) {
            role = role.substring(5); // "ROLE_ADMIN" → "ADMIN"
        }

        // 4️⃣ Construction de l'objet compatible avec Spring Security
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword()) // déjà encodé en BCrypt
                .roles(role)                  // ex : "ADMIN"
                .build();
    }
}
