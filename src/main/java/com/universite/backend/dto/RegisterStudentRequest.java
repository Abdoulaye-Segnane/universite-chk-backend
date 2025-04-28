package com.universite.backend.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter


public class RegisterStudentRequest {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 5, message = "Le mot de passe doit contenir au moins 5 caractères")
    private String password;

    @Email(message = "Email invalide")
    private String email;

    @Column(nullable = false, unique = true)
    private String ine; //  unique

}
