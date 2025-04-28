package com.universite.backend.repository;

import com.universite.backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Aucune méthode supplémentaire pour l’instant
    // findAll(), findById(), save(), delete() déjà héritées de JpaRepository
}
