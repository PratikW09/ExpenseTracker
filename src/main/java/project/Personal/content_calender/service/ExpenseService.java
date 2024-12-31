package project.Personal.content_calender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.Personal.content_calender.entity.ExpenseEntity;
import project.Personal.content_calender.repository.ExpenseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * Creates a new expense and saves it to the database.
     * @param expense the expense entity to be saved
     * @return the saved expense entity
     */
    public ExpenseEntity createExpense(ExpenseEntity expense) {
        // Set the current date as the expense date if not already set
        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now());
        }

        // Save the expense to the database
        return expenseRepository.save(expense);
    }

    /**
     * Retrieves an expense by its unique ID.
     * @param id the unique ID of the expense
     * @return an Optional containing the expense if found, or empty if not
     */
    public Optional<ExpenseEntity> getExpenseById(String id) {
        return expenseRepository.findById(id);
    }

    /**
     * Retrieves all expenses.
     * @return a list of all expenses
     */
    public List<ExpenseEntity> getAllExpenses() {
        return expenseRepository.findAll();
    }

    /**
     * Retrieves expenses by category.
     * @param category the category of the expenses to retrieve
     * @return a list of expenses in the specified category
     */
    public List<ExpenseEntity> getExpensesByCategory(String category) {
        return expenseRepository.findAllByCategory(category);
    }

    /**
     * Deletes an expense by its ID.
     * @param id the unique ID of the expense to be deleted
     */
    public void deleteExpenseById(String id) {
        expenseRepository.deleteById(id);
    }

    /**
     * Updates the details of an existing expense.
     * @param id the unique ID of the expense to be updated
     * @param updatedExpense the updated expense entity
     * @return the updated expense entity
     */
    public ExpenseEntity updateExpense(String id, ExpenseEntity updatedExpense) {
        return expenseRepository.findById(id)
                .map(existingExpense -> {
                    // Update fields with new values
                    existingExpense.setAmount(updatedExpense.getAmount());
                    existingExpense.setCategory(updatedExpense.getCategory());
                    existingExpense.setDescription(updatedExpense.getDescription());
                    existingExpense.setDate(updatedExpense.getDate());
                    existingExpense.setUpdatedAt(LocalDateTime.now()); // Update timestamp
                    return expenseRepository.save(existingExpense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    /**
     * Retrieves total expenditure (sum of all expenses).
     * @return the total expenditure as a double
     */
    public double getTotalExpenditure() {
        List<ExpenseEntity> expenses = expenseRepository.findAll();
        return expenses.stream().mapToDouble(ExpenseEntity::getAmount).sum();
    }

    /**
     * Retrieves all expenses for a specific user.
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a list of expenses for the specified user
     */
    public List<ExpenseEntity> getExpensesByUserId(String userId) {
        // Assuming that ExpenseEntity has a 'user' field which is of type UserEntity
        return expenseRepository.findAllByUserId(userId);
    }
}
