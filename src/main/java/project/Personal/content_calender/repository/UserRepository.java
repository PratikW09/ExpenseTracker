package project.Personal.content_calender.repository;



import org.springframework.data.mongodb.repository.MongoRepository;

import project.Personal.content_calender.entity.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String>{
    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return the UserEntity with the specified email, or null if not found
     */
    UserEntity findByEmail(String email);
}

