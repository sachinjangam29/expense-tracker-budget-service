package org.expense.tracker.budget.service.Exception.Exceptionclasses;

public class BudgetForGivenDatesNotFound extends RuntimeException{
    public BudgetForGivenDatesNotFound(String message) {
        super(message);
    }
}
