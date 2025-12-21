package ecom_blog.mapper;

import ecom_blog.dto.RegisterUserDto;
import ecom_blog.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterUserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setNom(dto.getNom());
        user.setPassword(dto.getPassword());
        user.setTelephone(dto.getTelephone());
        return user;
    }
}
