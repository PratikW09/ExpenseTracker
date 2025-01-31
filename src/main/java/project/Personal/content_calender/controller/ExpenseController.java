package project.Personal.content_calender.controller;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import project.Personal.content_calender.entity.ExpenseEntity;
// import project.Personal.content_calender.entity.UserEntity;
// import project.Personal.content_calender.repository.UserRepository;
import project.Personal.content_calender.service.ExpenseService;
// import project.Personal.content_calender.service.UserService;
// import project.Personal.content_calender.utils.JwtUtils;

import java.util.List;
import java.util.Optional;

import project.Personal.content_calender.service.JWTService;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    // @Autowired
    // private UserService userService;

    @Autowired
    private JWTService jwtService;

    // @Autowired
    // private UserRepository userRepo;

    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@RequestHeader("Authorization") String token,
            @RequestBody ExpenseEntity expense) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove the "Bearer " prefix if present
        }
        logger.info("Received request to create expense: {}", expense.getDescription());
        try {

            String userEmail = jwtService.extractEmail(token);
            String userId = jwtService.extractUserId(token);

            System.out.println("UserEmail from create expense: " + userEmail);
            System.out.println("UserEmail from create expense: " + userId);

            if (userEmail == null || userEmail.isEmpty()) {
                logger.warn("Invalid or missing user ID in token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user token");
            }

            expense.setUserId(userId); // ✅ Assign the entire UserEntity object
            ExpenseEntity createdExpense = expenseService.createExpense(expense);
            logger.info("Expense created successfully for user {}: {}", userId, createdExpense);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } catch (Exception e) {
            logger.error("Error creating expense: {}", expense.getDescription(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create expense");
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllExpenses() {
        logger.info("Received request to fetch all expenses");
        List<ExpenseEntity> expenses = expenseService.getAllExpenses();
        if (expenses.isEmpty()) {
            logger.warn("No expenses found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expenses found");
        }
        logger.info("Expenses retrieved successfully");
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/get-by-category/{category}")
    public ResponseEntity<?> getExpensesByCategory(@PathVariable String category) {
        logger.info("Received request to fetch expenses by category: {}", category);

        List<ExpenseEntity> expenses = expenseService.getExpensesByCategory(category);
        if (expenses.isEmpty()) {
            logger.warn("No expenses found for category: {}", category);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expenses found for this category");
        }
        logger.info("Expenses found for category: {}", category);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<?> getExpensesByUserId(@PathVariable String userId) {

        logger.info("Received request to fetch expenses for user ID: {}", userId);

        List<ExpenseEntity> expenses = expenseService.getExpensesByUserId(userId);
        if (expenses.isEmpty()) {
            logger.warn("No expenses found for user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No expenses found for this user");
        }
        logger.info("Expenses found for user ID: {}", userId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable String id) {
        logger.info("Received request to fetch expense by ID: {}", id);

        Optional<ExpenseEntity> expense = expenseService.getExpenseById(id);
        if (expense.isPresent()) {
            logger.info("Expense found with ID: {}", id);
            return ResponseEntity.ok(expense.get());
        }
        logger.warn("No expense found with ID: {}", id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable String id, @RequestBody ExpenseEntity updatedExpense) {
        logger.info("Received request to update expense with ID: {}", id);
        try {

            ExpenseEntity expense = expenseService.updateExpense(id, updatedExpense);
            logger.info("Expense updated successfully: {}", expense.getId());
            return ResponseEntity.ok(expense);
        } catch (RuntimeException e) {
            logger.error("Expense with ID not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense not found");
        } catch (Exception e) {
            logger.error("Error updating expense with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update expense");
        }
    }

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

    @GetMapping("/total-expenditure")
    public ResponseEntity<?> getTotalExpenditure() {
        logger.info("Received request to fetch total expenditure");
        double totalExpenditure = expenseService.getTotalExpenditure();
        logger.info("Total expenditure retrieved successfully: {}", totalExpenditure);
        return ResponseEntity.ok(totalExpenditure);
    }
}
