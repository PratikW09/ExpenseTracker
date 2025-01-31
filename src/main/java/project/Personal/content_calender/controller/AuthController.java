package project.Personal.content_calender.controller;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.Personal.content_calender.entity.UserEntity;
import project.Personal.content_calender.service.UserService;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(5);

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        // Validate input
        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name cannot be null or empty.");
        }
        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be null or empty.");
        }

        try {
            // Hash the password before saving
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            UserEntity createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            logger.error("Error creating user: {}", user.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user.");
        }
    }

    @PostMapping("/login")
public ResponseEntity<String> login(@RequestBody UserEntity user) {
    // Validate input
    if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address.");
    }
    if (user.getPassword() == null || user.getPassword().isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be null or empty.");
    }

    
    String loginResponse = userService.verifyLogin(user);
    return ResponseEntity.ok(loginResponse);  
}

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // Get the session if it exists

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok("Logout successful.");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
