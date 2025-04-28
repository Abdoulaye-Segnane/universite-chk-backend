package com.universite.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String username;
    private String email;
    private String ine;
}
