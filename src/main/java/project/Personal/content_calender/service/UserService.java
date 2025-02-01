package project.Personal.content_calender.service;

import java.util.List;
import java.util.Map;
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

    public Optional<UserEntity> updateUser(String userId, Map<String, Object> updates) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            updates.forEach((key, value) -> {
                switch (key) {
                    case "name" -> user.setName((String) value);
                    case "targetExpense" -> user.setTargetExpense(Double.valueOf(value.toString()));
                    
                }
            });

            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }
    
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    
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

    public Optional<UserEntity> getCurrUser(String token) {
        // Extract user email from token
        String userEmail = jwtService.extractEmail(token);
    
        // Find user by email, wrapped in an Optional to handle the case where the user might not be found
        return Optional.ofNullable(userRepository.findByEmail(userEmail));
    }
    
    
}
