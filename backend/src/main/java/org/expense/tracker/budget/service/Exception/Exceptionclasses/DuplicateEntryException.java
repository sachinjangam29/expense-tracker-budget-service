package org.expense.tracker.budget.service.Exception.Exceptionclasses;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
