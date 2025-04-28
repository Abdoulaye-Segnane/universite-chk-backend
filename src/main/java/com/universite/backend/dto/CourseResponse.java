package com.universite.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private String titre;
    private String description;
    private LocalDate date;
    private String formateur;
}
