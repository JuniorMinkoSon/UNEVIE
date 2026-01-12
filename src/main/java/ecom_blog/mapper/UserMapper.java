package ecom_blog.mapper;

import ecom_blog.dto.UserDto;
import ecom_blog.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nom(user.getNom())
                .role(user.getRole())
                // On ne mappe généralement pas le mot de passe vers le DTO pour des raisons de
                // sécurité,
                // mais ici on peut en avoir besoin pour la création initiale ou certains flux
                // spécifiques.
                // .password(user.getPassword())
                .build();
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setNom(userDto.getNom());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }
}
