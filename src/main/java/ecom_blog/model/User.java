package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter @Setter
@ToString
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String email;
    private String nom;
    private String password;
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
