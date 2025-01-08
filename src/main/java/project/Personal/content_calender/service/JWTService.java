package project.Personal.content_calender.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JWTService {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generates a random secret key

    public String generateJWTToken(String user_email) {
        String issuer = "content_calendar_app"; // Your application's name or identifier
        String subject = user_email; // Can be a specific user's identifier

        // Set expiration to 1 hour from the current time
        long expirationMillis = System.currentTimeMillis() + (60 * 60 * 1000);

        // Generate the JWT token
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(new Date()) // Current timestamp
                .setExpiration(new Date(expirationMillis)) // Expiration timestamp
                .signWith(secretKey) // Signing the token with the secret key
                .compact(); // Converts it to a string
    }
}
