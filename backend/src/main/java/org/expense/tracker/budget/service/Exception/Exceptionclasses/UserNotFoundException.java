package org.expense.tracker.budget.service.Exception.Exceptionclasses;


public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
