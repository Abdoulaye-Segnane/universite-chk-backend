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

    @Column(nullable = false, unique = true)
    private String ine; //  unique

    @Column(nullable = true)
    private String formation; // Ex : Informatique, Mathématiques...

    @Column(nullable = true)
    private LocalDate dateNaissance;
}
