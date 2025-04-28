package com.universite.backend.controller;

import com.universite.backend.dto.StudentResponse;
import com.universite.backend.entity.Student;
import com.universite.backend.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // 🔐 Créer un étudiant (déjà fait)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createStudent(@Valid @RequestBody StudentResponse request) {
        return ResponseEntity.badRequest().body("Déjà traité dans AuthController, ici uniquement GET.");
    }

    // 📚 Lister tous les étudiants - réservé à l'ADMIN
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        List<StudentResponse> response = students.stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getUsername(),
                        student.getEmail(),
                        student.getIne()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
