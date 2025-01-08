package project.Personal.content_calender.controller;

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
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder password_encoder = new BCryptPasswordEncoder(5);

    /**
     * Create a new user.
     * @param user the user entity to be created
     * @return ResponseEntity containing the created user and HTTP status
     */

     @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {
        logger.info("Received request to create user: {}", user.getName());

        // Validate input
        if (user.getName() == null || user.getName().isEmpty()) {
            logger.error("Name cannot be null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Name cannot be null or empty");
        }
        if (user.getEmail() == null || !isValidEmail(user.getEmail())) {
            logger.error("Invalid email address: {}", user.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            logger.error("Password cannot be null or empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password cannot be null or empty");
        }

        try {
            // Hash the password
            String hashedPassword = password_encoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            // Set created and updated timestamps
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // Save user to the database
            UserEntity createdUser = userService.createUser(user);
            logger.info("User created successfully with ID: {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            logger.error("Error creating user: {}", user.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    // Utility to validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user){
        return userService.verifyLogin(user);

    }



    /**
     * Retrieve a user by ID.
     * @param id the unique ID of the user to fetch
     * @return ResponseEntity with the user details or 404 if not found
     */
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        logger.info("Received request to fetch user by ID: {}", id);
        Optional<UserEntity> user = userService.getUserById(id);
        if (user.isPresent()) {
            logger.info("User found with ID: {}", id);
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("User not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    /**
     * Delete a user by ID.
     * @param id the unique ID of the user to delete
     * @return ResponseEntity with success or error message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        logger.info("Received request to delete user with ID: {}", id);
        try {
            userService.deleteUserById(id);
            logger.info("User deleted successfully with ID: {}", id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    /**
     * Update user details.
     * @param id the unique ID of the user to update
     * @param updatedUser the updated user entity
     * @return ResponseEntity with updated user details or error message
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserEntity updatedUser) {
        logger.info("Received request to update user with ID: {}", id);
        try {
            UserEntity user = userService.updateUser(id, updatedUser);
            logger.info("User updated successfully with ID: {}", user.getId());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

    /**
     * Retrieve all users (for future use, if required).
     * @return ResponseEntity with the list of all users
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        logger.info("Received request to fetch all users");
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            logger.error("Error fetching all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch users");
        }
    }

    /**
     * Search for a user by email.
     * @param email the email to search for
     * @return ResponseEntity with user details or 404 if not found
     */
//     @GetMapping("/search-by-email/{email}")
// public ResponseEntity<?> searchUserByEmail(@PathVariable String email) {
//     logger.info("Received request to search user by email: {}", email);
    
//     Optional<UserEntity> user = userService.getUserByEmail(email);
    
//     if (user.isPresent()) {
//         logger.info("User found with email: {}", email);
//         return ResponseEntity.ok(user.get());  // Get the user entity from Optional
//     } else {
//         logger.warn("User not found with email: {}", email);
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//     }
// }

}
