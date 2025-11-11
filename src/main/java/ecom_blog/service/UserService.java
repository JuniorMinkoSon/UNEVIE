package ecom_blog.service;

import ecom_blog.model.User;

public interface UserService {
    User saveUser(User user);
    User findByEmail(String email);
}
