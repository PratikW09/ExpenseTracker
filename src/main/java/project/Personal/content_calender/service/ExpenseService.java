package project.Personal.content_calender.service;

import org.bson.types.ObjectId;
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

    public ExpenseEntity createExpense(ExpenseEntity expense) {
            expense.setCreatedAt(LocalDateTime.now());
            expense.setUpdatedAt(LocalDateTime.now());
        
        return expenseRepository.save(expense);
    }

    public Optional<ExpenseEntity> getExpenseById(String id) {
        return expenseRepository.findById(id);
    }

    public List<ExpenseEntity> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<ExpenseEntity> getExpensesByCategory(String category) {
        return expenseRepository.findByCategory(category);
    }

    public void deleteExpenseById(String id) {
        expenseRepository.deleteById(id);
    }

    public ExpenseEntity updateExpense(String id, ExpenseEntity updatedExpense) {
        return expenseRepository.findById(id)
                .map(existingExpense -> {
                    existingExpense.setAmount(updatedExpense.getAmount());
                    existingExpense.setCategory(updatedExpense.getCategory());
                    existingExpense.setDescription(updatedExpense.getDescription());
                    existingExpense.setCreatedAt(updatedExpense.getCreatedAt());
                    existingExpense.setUpdatedAt(LocalDateTime.now());
                    return expenseRepository.save(existingExpense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    public double getTotalExpenditure() {
        return expenseRepository.findAll().stream().mapToDouble(ExpenseEntity::getAmount).sum();
    }

    // public List<ExpenseEntity> getExpensesByUserId(String userId) {
    // return expenseRepository.findAllByUserEmail(userId);
    // }

    public List<ExpenseEntity> getExpensesByUserId(String userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}