package ecom_blog.service;

import ecom_blog.dto.UserDto;
import ecom_blog.model.User;

public interface UserService {
    User saveUser(UserDto userDto);

    UserDto findByEmail(String email);
}
