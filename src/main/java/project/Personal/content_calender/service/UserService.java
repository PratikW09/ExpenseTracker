package project.Personal.content_calender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Personal.content_calender.entity.UserEntity;
import project.Personal.content_calender.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user in the database.
     * @param user the user entity to be created
     * @return the saved user entity
     */
    public UserEntity createUser(UserEntity user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     * @param id the unique ID of the user
     * @return an Optional containing the user entity, if found
     */
    public Optional<UserEntity> getUserById(String id) {
        return userRepository.findById(id);
    }

    /**
     * Deletes a user by their ID.
     * @param id the unique ID of the user to be deleted
     */
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    /**
     * Updates the details of an existing user.
     * @param id the unique ID of the user to be updated
     * @param updatedUser the user entity containing updated information
     * @return the updated user entity
     * @throws RuntimeException if the user is not found
     */
    public UserEntity updateUser(String id, UserEntity updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.updateTimestamp(); // Ensure that the timestamp is updated
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Retrieves all users in the database.
     * @return a list of all user entities
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their email.
     * @param email the email of the user to be retrieved
     * @return an Optional containing the user entity, if found
     */
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
