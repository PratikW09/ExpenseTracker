package project.Personal.content_calender.service;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import project.Personal.content_calender.entity.UserEntity;
import project.Personal.content_calender.repository.UserRepository;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    /**
     * Creates a new user in the database.
     * 
     * @param user the user entity to be created
     * @return the saved user entity
     */
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    
    public Optional<UserEntity> getUserById(String id) {
        return userRepository.findById(id);
    }

   
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

   
    public UserEntity updateUser(String id, UserEntity updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    // Update the existing user fields with the values from updatedUser
                    existingUser.setName(updatedUser.getName());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setTargetExpense(updatedUser.getTargetExpense()); // Add any other fields that should
                                                                                   // be updated

                    // Update the timestamp for the last modification
                    existingUser.updateTimestamp();

                    // Save the updated user entity to the database
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Retrieves all users in the database.
     * 
     * @return a list of all user entities
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their email.
     * 
     * @param email the email of the user to be retrieved
     * @return an Optional containing the user entity, if found
     */
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String verifyLogin(UserEntity user) {
        System.out.println("in userService verfiy login controller");
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        System.out.println("verfiy Login : " + authentication);
        if (authentication.isAuthenticated()) {
            UserEntity userDB = userRepository.findByEmail(user.getEmail());
            return jwtService.generateToken(userDB);
        }
        return "fail from verfylogin service";
    }
}
