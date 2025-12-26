package ecom_blog.service;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        // Only encode if it's not already encoded (BCrypt hashes start with $2a$ or
        // $2y$ or $2b$)
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")
                && !user.getPassword().startsWith("$2y$") && !user.getPassword().startsWith("$2b$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_USER);
        }
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() { // ✅ ajout de cette méthode
        return userRepository.findAll();
    }

    @Override
    public long count() { // ✅ ajout de cette méthode
        return userRepository.count();
    }
}
