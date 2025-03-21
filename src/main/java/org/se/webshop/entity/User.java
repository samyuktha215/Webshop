package org.se.webshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userName;
    private String password;
    private String role;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @OneToMany(mappedBy = "user")
    List<Order> orders;





}
