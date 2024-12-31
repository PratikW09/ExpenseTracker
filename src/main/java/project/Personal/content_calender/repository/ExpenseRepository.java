package project.Personal.content_calender.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import project.Personal.content_calender.entity.ExpenseEntity;

public interface ExpenseRepository extends MongoRepository<ExpenseEntity, String> {

    public List<ExpenseEntity> findAllByCategory(String category);
    // You can add custom queries here if needed

    public List<ExpenseEntity> findAllByUserId(String userId);
}
