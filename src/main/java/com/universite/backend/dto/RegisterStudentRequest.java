package com.universite.backend.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter


public class RegisterStudentRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;


    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;


    @Column(nullable = false, unique = true)
    private int anneeBac;
}
