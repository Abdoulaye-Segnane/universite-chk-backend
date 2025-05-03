package com.universite.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String username;
    private String email;
    private String ine;
    private String motDePasseInitial;
    private int anneeBac;
}
