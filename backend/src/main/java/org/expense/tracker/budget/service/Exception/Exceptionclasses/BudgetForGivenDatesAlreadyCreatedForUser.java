package org.expense.tracker.budget.service.Exception.Exceptionclasses;

public class BudgetForGivenDatesAlreadyCreatedForUser extends RuntimeException{
    public BudgetForGivenDatesAlreadyCreatedForUser(String message) {
        super(message);
    }
}
