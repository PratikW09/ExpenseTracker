package project.Personal.content_calender.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import project.Personal.content_calender.entity.UserEntity;
import io.jsonwebtoken.JwtException;

@Service
public class JWTService {

    // Securely store the secret key in an environment variable or configuration
    private final String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$VTaK+HaV^uvCHEFsE"; // Ensure it's at least 32
                                                                                          // characters

    // Helper Method to Retrieve the Signing Key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extract Email (Subject) from Token
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("email", String.class); // Extract the email directly from claims
    }

    // Extract Role from Token
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class); // Extract the role directly from claims
    }

    // Extract User ID from Token
    public String extractUserId(String token) {
        Claims claims = extractAllClaims(token);

        Object userId = claims.get("userId"); // Extract as Object first
        System.out.println("Extracted User ID: " + userId);

        return (userId != null) ? userId.toString() : "Id error ";
    }

    // Extract Expiration Date from Token
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // Extract All Claims from Token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid token: " + e.getMessage(), e);
        }
    }

    // Check if Token is Expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate Token for a Given User
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();

        // Add user details to claims
        System.out.println("userId from genrate token : " + user.getId());
        claims.put("userId", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return createToken(claims, user); // Email as the subject
    }

    // Create Token with Claims and Subject
    private String createToken(Map<String, Object> claims, UserEntity user) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // Set the subject as email (or any unique identifier)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiration
                .setHeaderParam("typ", "JWT") // Set JWT type
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign the token
                .compact();
    }

    // Validate Token and Check if Email Matches
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove the "Bearer " prefix if present
            }
            final String extractedEmail = extractEmail(token);

            // Validate if the email, role, and userId match and if the token is not expired
            return (extractedEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false; // Return false if validation fails
        }
    }
}
