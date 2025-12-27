package ecom_blog.service;

import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import ecom_blog.security.CustomUserDetails;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null || !user.isActif()) {
            throw new UsernameNotFoundException("Utilisateur invalide");
        }

        return new CustomUserDetails(user);
    }
}
