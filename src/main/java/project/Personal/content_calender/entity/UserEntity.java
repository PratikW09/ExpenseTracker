package project.Personal.content_calender.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the user entity for the finance tracker application.
 * This class maps to the "users" collection in MongoDB.
 */
@Data
@NoArgsConstructor
@Document(collection = "users")
public class UserEntity {

    @Id
    private String id; // Unique identifier for the user (MongoDB _id)

    @Indexed(unique = true) // Ensures unique email index in MongoDB
    private String email; // User's email, must be unique

    private String name; // User's name
   

    private String password; // Encrypted password of the user

    @CreatedDate
    private LocalDateTime createdAt; // Timestamp for when the user is created

    @LastModifiedDate
    private LocalDateTime updatedAt; // Timestamp for when the user is updated
    // String getEmail;

    /**
     * Manually updates the 'updatedAt' timestamp to the current time.
     */
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now(); // Manually set the 'updatedAt' to the current time
    }
}
