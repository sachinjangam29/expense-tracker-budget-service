package org.expense.tracker.budget.service.service;

import org.expense.tracker.budget.service.Exception.Exceptionclasses.BudgetForGivenDatesAlreadyCreatedForUser;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.DuplicateEntryException;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.UserNotFoundException;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.ValidDateException;
import org.expense.tracker.budget.service.UserClient;
import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.repository.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public BudgetService(BudgetRepository budgetRepository,
                         UserClient userClient) {
        this.budgetRepository = budgetRepository;
        this.userClient = userClient;
    }

    public Budget addBudget(BudgetRequest budgetRequest) {
        BigInteger userId = getUserId(budgetRequest.getUsername());
        if (userId == null) {
            throw new UserNotFoundException("User ID not found for username: " + budgetRequest.getUsername());
        }

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

    public BigInteger getUserId(String username) {
        return userClient.getUserId(username);
    }
}
