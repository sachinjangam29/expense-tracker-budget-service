package org.expense.tracker.budget.service.service;

import org.expense.tracker.budget.service.UserClient;
import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserClient userClient;

    public BudgetService(BudgetRepository budgetRepository,
                         UserClient userClient) {
        this.budgetRepository = budgetRepository;
        this.userClient = userClient;
    }

    public Budget addBudget(BudgetRequest budgetRequest) {
        Budget budget = new Budget();

        BigInteger userId = getUserId(budgetRequest.getUsername());
        if (userId != null) {
            budget.setUserId(BigInteger.valueOf(1231));
            budget.setStartDate(budgetRequest.getStartDate());
            budget.setEndDate(budgetRequest.getEndDate());
            budget.setAllocatedAmount(budgetRequest.getAllocatedAmount());
            return budgetRepository.save(budget);
        } else {
            return null;
        }

    }

    public BigInteger getUserId(String username) {
        return userClient.getUserId(username);
    }
}
