package project.Personal.content_calender.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import project.Personal.content_calender.entity.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    // Find user by email
    UserEntity findByEmail(String email);

    // Check if a user exists by email (for registration)
    boolean existsByEmail(String email);

    // Find user by ID (for referencing or fetching user details by their unique ID)
    Optional<UserEntity> findById(String id); // Return type should be Optional<UserEntity>
}
