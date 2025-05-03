package com.universite.backend.controller;

import com.universite.backend.config.JwtUtil;
import com.universite.backend.dto.ApiResponse;
import com.universite.backend.dto.LoginRequest;
import com.universite.backend.dto.LoginResponse;
import com.universite.backend.entity.User;
import com.universite.backend.repository.UserRepository;
import com.universite.backend.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentification", description = "Connexion & demande de réinitialisation")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Connexion avec identifiants valides")
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

    @PostMapping("/forgot-password")
    @Operation(summary = "Demande de mot de passe", description = "Un email est envoyé à l'administration")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Aucun compte trouvé avec cet email.", "error", 400));
        }

        // Notification à l'admin
        String adminEmail = "segnanelaye@gmail.com";
        String subject = "🔐 Demande de réinitialisation de mot de passe";
        String content = "L'utilisateur avec l'email " + email + " a demandé une réinitialisation de mot de passe.";

        emailService.sendSimpleEmail(adminEmail, subject, content);

        return ResponseEntity.ok(new ApiResponse("Votre demande a été envoyée à l'administration.", "success", 200));
    }
}
