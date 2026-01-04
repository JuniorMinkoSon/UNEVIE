package ecom_blog.service;

import ecom_blog.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    User findByEmail(String email);

    Optional<User> findByEmailOptional(String email);

    List<User> getAllUsers();

    long count();

    User updateProfile(Long id, String nom, String telephone);
}
