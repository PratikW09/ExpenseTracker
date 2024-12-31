package project.Personal.content_calender.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.Personal.content_calender.entity.ExpenseEntity;
import project.Personal.content_calender.service.ExpenseService;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling expense-related HTTP requests.
 * It includes functionality to create, read, update, delete, and search expenses.
 */
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    /**
     * Create a new expense.
     * @param expense the expense data to be created
     * @return the created expense
     */
    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseEntity expense) {
        logger.info("Received request to create expense: {}", expense.getDescription());
        try {
            ExpenseEntity createdExpense = expenseService.createExpense(expense);
            logger.info("Expense created successfully: {}", createdExpense.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } catch (Exception e) {
            logger.error("Error creating expense: {}", expense.getDescription(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create expense");
        }
    }

    /**
     * Retrieve all expenses by category.
     * @param category the category of expenses to retrieve
     * @return a list of expenses in the specified category
     */
    @GetMapping("/get-by-category/{category}")
    public ResponseEntity<?> getExpensesByCategory(@PathVariable String category) {
        logger.info("Received request to fetch expenses by category: {}", category);
        List<ExpenseEntity> expenses = expenseService.getExpensesByCategory(category);
        if (expenses.isEmpty()) {
            logger.warn("No expenses found for category: {}", category);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expenses found for this category");
        } else {
            logger.info("Expenses found for category: {}", category);
            return ResponseEntity.ok(expenses);
        }
    }

    /**
     * Retrieve all expenses by user ID.
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a list of expenses for the specified user
     */
    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<?> getExpensesByUserId(@PathVariable String userId) {
        logger.info("Received request to fetch expenses for user ID: {}", userId);
        List<ExpenseEntity> expenses = expenseService.getExpensesByUserId(userId);
        if (expenses.isEmpty()) {
            logger.warn("No expenses found for user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expenses found for this user");
        } else {
            logger.info("Expenses found for user ID: {}", userId);
            return ResponseEntity.ok(expenses);
        }
    }

    /**
     * Retrieve an expense by ID.
     * @param id the unique ID of the expense
     * @return the expense if found
     */
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable String id) {
        logger.info("Received request to fetch expense by ID: {}", id);
        Optional<ExpenseEntity> expense = expenseService.getExpenseById(id);
        if (expense.isPresent()) {
            logger.info("Expense found with ID: {}", id);
            return ResponseEntity.ok(expense.get());
        } else {
            logger.warn("No expense found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        }
    }

    /**
     * Update an existing expense by ID.
     * @param id the unique ID of the expense to update
     * @param updatedExpense the updated expense data
     * @return the updated expense
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable String id, @RequestBody ExpenseEntity updatedExpense) {
        logger.info("Received request to update expense with ID: {}", id);
        try {
            ExpenseEntity expense = expenseService.updateExpense(id, updatedExpense);
            logger.info("Expense updated successfully: {}", expense.getId());
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            logger.error("Error updating expense with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update expense");
        }
    }

    /**
     * Delete an expense by ID.
     * @param id the unique ID of the expense to delete
     * @return a confirmation message
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable String id) {
        logger.info("Received request to delete expense with ID: {}", id);
        try {
            expenseService.deleteExpenseById(id);
            logger.info("Expense deleted successfully: {}", id);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting expense with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete expense");
        }
    }
}
