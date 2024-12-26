package org.expense.tracker.budget.service.service;

import org.expense.tracker.budget.service.Exception.Exceptionclasses.*;
import org.expense.tracker.budget.service.UserClient;
import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.repository.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserClient userClient;
    private final Logger logger = LoggerFactory.getLogger(BudgetService.class);
    private  BigInteger userId = null;

    public BudgetService(BudgetRepository budgetRepository,
                         UserClient userClient) {
        this.budgetRepository = budgetRepository;
        this.userClient = userClient;
    }

    public Budget addBudget(BudgetRequest budgetRequest) {
        userId = getValidatedUser(budgetRequest.getUsername());


        LocalDate startDate = budgetRequest.getStartDate();
        LocalDate endDate = budgetRequest.getEndDate();

        // Validation: Start and End Dates
        if (endDate.isBefore(startDate)) {
            throw new ValidDateException("End date cannot be before start date. Please provide valid dates.");
        }
        if (budgetRepository.checkValidStartDate(startDate,userId)) {
            throw new ValidDateException("Start date should be greater than the last end date. ");
        }

        // Validation: Overlapping Dates
        if (budgetRepository.existsByOverlappingDates(startDate, endDate, userId)) {
            throw new BudgetForGivenDatesAlreadyCreatedForUser(
                    "Budget already exists for the given date range: " + startDate + " to " + endDate);
        }

        // Create and Save Budget
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);
        budget.setAllocatedAmount(budgetRequest.getAllocatedAmount());

        try {
            return budgetRepository.save(budget);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new DuplicateEntryException("Duplicate entry for the user. Please check your data.");
            }
            throw ex;
        }
    }

    public int deleteBudgetForGivenDates(BudgetRequest budgetRequest) {
         userId = getValidatedUser(budgetRequest.getUsername());
        int countDeletedRecords = 0;

        LocalDate startDate = budgetRequest.getStartDate();
        LocalDate endDate = budgetRequest.getEndDate();
        if (endDate.isBefore(startDate)) {
            throw new ValidDateException("End date cannot be before start date. Please provide valid dates.");
        }
        try {
             countDeletedRecords = budgetRepository.deleteByStartAndEndDate(startDate, endDate, userId);
            if (countDeletedRecords == 0) {
                throw new BudgetForGivenDatesNotFound("Budgets for the given dates not found");
            }
        }catch (DataAccessException e) {  // Handle specific database exceptions
            logger.error("Database error while deleting budget: " + e.getMessage());
            throw e;  // Re-throw the exception
        }
        return countDeletedRecords;
    }

    private BigInteger getValidatedUser(String username) {
        BigInteger userId = getUserId(username);
        if(userId == null) {
            throw new UserNotFoundException("Username not available: {} " +username);
        }else {
            return userId;
        }
    }

    public BigInteger getUserId(String username) {
        return userClient.getUserId(username);
    }
}
