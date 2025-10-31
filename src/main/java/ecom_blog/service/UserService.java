package ecom_blog.service;

import ecom_blog.model.User;
import ecom_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepo;

    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User save(User u) { return userRepo.save(u); }
}
