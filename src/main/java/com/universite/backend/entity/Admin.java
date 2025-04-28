package com.universite.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @Column(nullable = true)
    private String department; // Ex : Scolarité, DCM, ...
}
