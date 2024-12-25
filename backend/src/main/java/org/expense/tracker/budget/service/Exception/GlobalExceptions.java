package org.expense.tracker.budget.service.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.expense.tracker.budget.service.Exception.ExceptionMessageFormat.ExceptionMessageFormat;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.BudgetForGivenDatesAlreadyCreatedForUser;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.DuplicateEntryException;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.UserNotFoundException;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.ValidDateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptions {
    private ExceptionMessageFormat exceptionMessageFormat;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionMessageFormat> userNotFound(Exception e, HttpServletRequest request) {
        exceptionMessageFormat = new ExceptionMessageFormat(HttpStatus.NOT_FOUND,request.getRequestURI(),e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionMessageFormat, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BudgetForGivenDatesAlreadyCreatedForUser.class)
    public ResponseEntity<ExceptionMessageFormat> budgetAlreadyCreatedForUser(Exception e, HttpServletRequest request) {
        exceptionMessageFormat = new ExceptionMessageFormat(HttpStatus.BAD_REQUEST,request.getRequestURI(),e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionMessageFormat, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<ExceptionMessageFormat> duplicateRecords(Exception e, HttpServletRequest request) {
        exceptionMessageFormat = new ExceptionMessageFormat(HttpStatus.BAD_REQUEST,request.getRequestURI(),e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionMessageFormat, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidDateException.class)
    public ResponseEntity<ExceptionMessageFormat> validDates(Exception e, HttpServletRequest request) {
        exceptionMessageFormat = new ExceptionMessageFormat(HttpStatus.BAD_REQUEST,request.getRequestURI(),e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(exceptionMessageFormat, HttpStatus.BAD_REQUEST);
    }

}
