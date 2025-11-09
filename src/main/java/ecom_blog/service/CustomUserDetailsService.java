package ecom_blog.service;

import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import ecom_blog.security.CustomUserDetails;
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
        // ✅ Recherche par email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Aucun utilisateur trouvé avec l'email : " + email);
        }

        // ✅ Retourne ton objet CustomUserDetails
        return new CustomUserDetails(user);
    }
}
