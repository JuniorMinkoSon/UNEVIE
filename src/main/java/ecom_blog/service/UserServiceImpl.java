package ecom_blog.service;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActif(true);

        // ⚙️ Paramètres spécifiques LIVREUR
        if (user.getRole() == Role.ROLE_LIVREUR) {
            user.setDisponible(true);
            user.setCongestionScore(0.0);
            user.setConsommationEstimee(0.0);
        }

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailOptional(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public User updateProfile(Long id, String nom, String telephone) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setNom(nom);
        user.setTelephone(telephone);
        return userRepository.save(user);
    }

    @Override
    public List<User> getDisponibleLivreurs() {
        return userRepository.findByRoleAndDisponible(Role.ROLE_LIVREUR, true)
                .stream()
                .filter(u -> u.getLatitude() != null && u.getLongitude() != null)
                .toList();
    }
}
