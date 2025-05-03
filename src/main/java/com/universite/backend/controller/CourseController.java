package com.universite.backend.controller;

import com.universite.backend.dto.ApiResponse;
import com.universite.backend.dto.CreateCourseRequest;
import com.universite.backend.dto.CourseResponse;
import com.universite.backend.entity.Course;
import com.universite.backend.repository.CourseRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cours")
@Tag(name = "Gestion des cours", description = "CRUD complet pour les entités Cours")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // 🛠 Créer un cours - réservé aux ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Créer un cours",
            description = "Accessible uniquement aux administrateurs."
    )
    public ResponseEntity<ApiResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        Course course = new Course();
        course.setTitre(request.getTitre());
        course.setDescription(request.getDescription());
        course.setDate(request.getDate());
        course.setFormateur(request.getFormateur());

        courseRepository.save(course);
        return ResponseEntity.ok(new ApiResponse("Cours créé avec succès !", "success", 200));
    }

    // 📚 Lister tous les cours - accessible à tous
    @GetMapping
    @Operation(
            summary = "Lister tous les cours",
            description = "Accessible à tous les utilisateurs connectés."
    )
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        List<CourseResponse> response = courses.stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitre(),
                        course.getDescription(),
                        course.getDate(),
                        course.getFormateur()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 🛠 Modifier un cours - réservé aux ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Modifier un cours",
            description = "Réservé aux administrateurs"
    )
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CreateCourseRequest request) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setTitre(request.getTitre());
                    course.setDescription(request.getDescription());
                    course.setDate(request.getDate());
                    course.setFormateur(request.getFormateur());
                    courseRepository.save(course);
                    return ResponseEntity.ok(new ApiResponse("Cours mis à jour avec succès !", "success", 200));
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Cours introuvable", "error", 404)));
    }

    // 🔎 Voir un cours par ID – utilisé pour modification ou consultation
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(
            summary = "Voir un cours par son ID",
            description = "Utilisé pour afficher ou modifier un cours existant."
    )
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    CourseResponse response = new CourseResponse(
                            course.getId(),
                            course.getTitre(),
                            course.getDescription(),
                            course.getDate(),
                            course.getFormateur()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🗑 Supprimer un cours - réservé aux ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Supprimer un cours",
            description = "Action irréversible, réservée à l'administration."
    )
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(course -> {
                    courseRepository.delete(course);
                    return ResponseEntity.ok(new ApiResponse("Cours supprimé avec succès !", "success", 200));
                })
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse("Cours introuvable", "error", 404)));
    }
}
