package com.universite.backend.config;

import com.universite.backend.entity.User;
import com.universite.backend.repository.UserRepository;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final String SECRET = "ma_super_cle_ultra_secrete_de_256_bits_minimum_pour_hmac";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_MS = 86400000;

    public String generateToken(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name()) // ✅ le rôle ici
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return getParser().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getParser().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JwtParser getParser() {
        return Jwts.parser().verifyWith((SecretKey) SECRET_KEY).build();
    }
}
