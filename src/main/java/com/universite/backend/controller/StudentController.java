package com.universite.backend.controller;

import com.universite.backend.dto.RegisterStudentRequest;
import com.universite.backend.dto.StudentResponse;
import com.universite.backend.dto.ApiResponse;
import com.universite.backend.entity.Role;
import com.universite.backend.entity.Student;
import com.universite.backend.repository.StudentRepository;
import com.universite.backend.utils.PasswordAndEmailGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Étudiants", description = "Gestion des comptes étudiants")
public class StudentController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentController(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer un étudiant", description = "Accessible uniquement aux admins")
    public ResponseEntity<ApiResponse> createStudent(@Valid @RequestBody RegisterStudentRequest request) {
        Student student = new Student();

        String username = capitalize(request.getNom()) + " " + capitalize(request.getPrenom());
        String password = PasswordAndEmailGenerator.generatePassword();
        String email = PasswordAndEmailGenerator.generateEmail(request.getNom(), request.getPrenom());
        String ine = PasswordAndEmailGenerator.generateINE(request.getAnneeBac());

        student.setNom(capitalize(request.getNom()));
        student.setPrenom(capitalize(request.getPrenom()));
        student.setAnneeBac(request.getAnneeBac());
        student.setUsername(username);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        student.setMotDePasseInitial(password);
        student.setIne(ine);
        student.setRole(Role.STUDENT);

        studentRepository.save(student);
        return ResponseEntity.ok(new ApiResponse("Étudiant créé avec succès !", "success", 200));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Lister les étudiants", description = "Liste tous les comptes étudiants")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        List<StudentResponse> response = students.stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getNom(),
                        student.getPrenom(),
                        student.getUsername(),
                        student.getEmail(),
                        student.getIne(),
                        student.getMotDePasseInitial(),
                        student.getAnneeBac()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(summary = "Obtenir un étudiant par ID")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    StudentResponse response = new StudentResponse(
                            student.getId(),
                            student.getNom(),
                            student.getPrenom(),
                            student.getUsername(),
                            student.getEmail(),
                            student.getIne(),
                            student.getMotDePasseInitial(),
                            student.getAnneeBac()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Modifier un étudiant", description = "Met à jour nom, prénom, email, anneeBac")
    public ResponseEntity<ApiResponse> updateStudent(@PathVariable Long id, @RequestBody RegisterStudentRequest request) {
        return studentRepository.findById(id)
                .map(student -> {
                    String username = capitalize(request.getNom()) + " " + capitalize(request.getPrenom());
                    String email = PasswordAndEmailGenerator.generateEmail(request.getNom(), request.getPrenom());

                    student.setNom(capitalize(request.getNom()));
                    student.setPrenom(capitalize(request.getPrenom()));
                    student.setUsername(username);
                    student.setEmail(email);
                    student.setAnneeBac(request.getAnneeBac());

                    studentRepository.save(student);
                    return ResponseEntity.ok(new ApiResponse("Étudiant modifié avec succès !", "success", 200));
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Étudiant introuvable", "error", 404)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un étudiant")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.ok(new ApiResponse("Étudiant supprimé avec succès !", "success", 200));
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Étudiant introuvable", "error", 404)));
    }
}
