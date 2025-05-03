package com.universite.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String ine; //  unique

    @Column(nullable = true)
    private String formation; // Ex : Informatique, Mathématiques...

    @Column(nullable = true)
    private LocalDate dateNaissance;

    @Column(nullable = false)
    private String motDePasseInitial;

    @Column(nullable = false)
    private int anneeBac;



}
