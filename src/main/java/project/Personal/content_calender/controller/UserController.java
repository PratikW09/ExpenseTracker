package project.Personal.content_calender.controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.Personal.content_calender.entity.ExpenseEntity;
import project.Personal.content_calender.entity.UserEntity;
import project.Personal.content_calender.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/get-current-user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        // Validate token
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid token format. Token should start with 'Bearer '");
        }

        token = token.substring(7); // Remove the "Bearer " prefix

        // Get the current user from the token
        Optional<UserEntity> userOpt = userService.getCurrUser(token);

        // Check if the user is present
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found for the provided token");
        }

        // Return the user if found
        return ResponseEntity.ok(userOpt.get());
    }

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

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody Map<String, Object> updates) {
        Optional<UserEntity> updatedUser = userService.updateUser(userId, updates);

        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    /**
     * Retrieve all users (for future use, if required).
     * 
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

}
