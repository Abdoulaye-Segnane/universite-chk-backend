package com.universite.backend.controller;

import com.universite.backend.dto.CreateCourseRequest;
import com.universite.backend.dto.CourseResponse;
import com.universite.backend.entity.Course;
import com.universite.backend.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cours")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // 🛠 Créer un cours - réservé aux ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        Course course = new Course();
        course.setTitre(request.getTitre());
        course.setDescription(request.getDescription());
        course.setDate(request.getDate());
        course.setFormateur(request.getFormateur());

        courseRepository.save(course);

        return ResponseEntity.ok("Cours créé avec succès !");
    }

    // 📚 Lister tous les cours - accessible à tous
    @GetMapping
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
}