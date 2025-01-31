package project.Personal.content_calender.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import project.Personal.content_calender.entity.ExpenseEntity;

public interface ExpenseRepository extends MongoRepository<ExpenseEntity, String> {
    List<ExpenseEntity> findByUserId(String userId);

    // Find all expenses for a user within a specific date range
    List<ExpenseEntity> findByUserIdAndUpdatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);

    // Find all expenses by category (e.g., "Food", "Entertainment", etc.)
    List<ExpenseEntity> findByCategory(String category);

    // Find an expense by its ID
    Optional<ExpenseEntity> findById(String expenseId);

    // Delete an expense by its ID
    
    void deleteById(String expenseId);

    // Count expenses for a user (helpful for statistics or totals)
    long countByUserId(String userId);
}
