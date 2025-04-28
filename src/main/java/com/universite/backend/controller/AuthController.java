package com.universite.backend.controller;

import com.universite.backend.config.JwtUtil;
import com.universite.backend.dto.LoginRequest;
import com.universite.backend.dto.LoginResponse;
import com.universite.backend.dto.RegisterStudentRequest;
import com.universite.backend.entity.Role;
import com.universite.backend.entity.Student;
import com.universite.backend.entity.User;
import com.universite.backend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
        return new LoginResponse(token, "Connexion réussie !");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterStudentRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Nom d'utilisateur déjà pris");
        }

        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.STUDENT);
        student.setIne(request.getIne()); // il faut que ton DTO contienne ine aussi !

        userRepository.save(student);

        return ResponseEntity.ok("Utilisateur enregistré avec succès");
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Aucun compte trouvé avec cet email.");
        }

        // TODO: Générer et envoyer un token de réinitialisation ici
        return ResponseEntity.ok("Un lien de réinitialisation a été envoyé si l'utilisateur existe.");
    }


}
