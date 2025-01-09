package com.intelliacademy.orizonroute.librarymanagmentsystem.model;

import com.intelliacademy.orizonroute.librarymanagmentsystem.model.enums.AdminRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private AdminRole role;

}
