package ecom_blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String email;
    private String nom;
    private String password;
    private String telephone;
}