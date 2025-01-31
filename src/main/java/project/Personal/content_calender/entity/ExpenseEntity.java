package project.Personal.content_calender.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Document(collection = "expenses")
public class ExpenseEntity {

    @Id
    private String id; // Using ObjectId for MongoDB compatibility

    @Indexed
    private String userId; // Reference to UserEntity (better than email)

    private Double amount; // Expense amount

    @Indexed
    private String category; // Indexed for better querying

    private String description; // Expense description

    private LocalDateTime createdAt; // Indexed for sorting expenses by date

    // @CreatedDate
    // private LocalDateTime createdAt;
    @Indexed
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
