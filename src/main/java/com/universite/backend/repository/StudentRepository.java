package com.universite.backend.repository;

import com.universite.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Rien d'autre à ajouter pour l’instant
}
