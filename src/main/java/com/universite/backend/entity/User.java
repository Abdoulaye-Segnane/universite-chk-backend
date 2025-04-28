package com.universite.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "user") // Nom de la table en base
@Inheritance(strategy = InheritanceType.JOINED) // ➔ pour l’héritage
@DiscriminatorColumn(name = "type")  // ➔ pour différencier Student/Admin
@DiscriminatorValue("USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email; //  champ email

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


}