package org.expense.tracker.budget.service.controller;

import org.expense.tracker.budget.service.Exception.Exceptionclasses.BudgetForGivenDatesNotFound;
import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.payload.Response.BudgetResponse;
import org.expense.tracker.budget.service.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private BudgetRequest budgetRequest;
    private Budget mockBudget;

    @BeforeEach
    public void setup() {
        budgetRequest = new BudgetRequest();
        budgetRequest.setUsername("testUser");
        budgetRequest.setStartDate(LocalDate.of(2024, 1, 1));
        budgetRequest.setEndDate(LocalDate.of(2024, 12, 31));
        budgetRequest.setAllocatedAmount(BigDecimal.valueOf(5000));

        mockBudget = new Budget();
        mockBudget.setUserId(BigInteger.valueOf(12345));
        mockBudget.setStartDate(budgetRequest.getStartDate());
        mockBudget.setEndDate(budgetRequest.getEndDate());
        mockBudget.setAllocatedAmount(budgetRequest.getAllocatedAmount());
        mockBudget.setCarryForward(BigDecimal.valueOf(200));
    }

    @Test
    void testAddBudgetEntry_withCarryForward() {
        // Arrange
        when(budgetService.addBudget(any(BudgetRequest.class))).thenReturn(mockBudget);

        // Act
        ResponseEntity<BudgetResponse> response = budgetController.addBudgetEntry(budgetRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BudgetResponse responseBody = response.getBody();
        assertEquals(mockBudget.getUserId(), responseBody.getUserId());
        assertEquals(mockBudget.getStartDate(), responseBody.getStartDate());
        assertEquals(mockBudget.getEndDate(), responseBody.getEndDate());
        assertEquals(mockBudget.getAllocatedAmount(), responseBody.getAllocatedAmount());
        assertEquals(mockBudget.getCarryForward(), responseBody.getCarryForward());
        assertEquals("Budget saved successfully...", responseBody.getMessage());

        verify(budgetService, times(1)).addBudget(any(BudgetRequest.class));
    }

    @Test
    void testAddBudgetEntry_withoutCarryForward() {

        mockBudget.setCarryForward(null); // No carry forward value

        when(budgetService.addBudget(any(BudgetRequest.class))).thenReturn(mockBudget);

        // Act
        ResponseEntity<BudgetResponse> response = budgetController.addBudgetEntry(budgetRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BudgetResponse responseBody = response.getBody();
        assertEquals(mockBudget.getUserId(), responseBody.getUserId());
        assertEquals(mockBudget.getStartDate(), responseBody.getStartDate());
        assertEquals(mockBudget.getEndDate(), responseBody.getEndDate());
        assertEquals(mockBudget.getAllocatedAmount(), responseBody.getAllocatedAmount());
        assertEquals(BigDecimal.ZERO, responseBody.getCarryForward()); // Expected default value
        assertEquals("Budget saved successfully...", responseBody.getMessage());

        verify(budgetService, times(1)).addBudget(any(BudgetRequest.class));
    }

    @Test
    public void testDeleteBudgetEntry(){
        when(budgetService.deleteBudgetForGivenDates(budgetRequest)).thenReturn(1);
        ResponseEntity<BudgetResponse> response = budgetController.deleteBudget(budgetRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Budget deleted successfully for the user: testUser", response.getBody().getMessage());
        verify(budgetService, Mockito.times(1)).deleteBudgetForGivenDates(budgetRequest);
    }

    @Test
    public void testDeleteBudgetEntry_Failure(){
        when(budgetService.deleteBudgetForGivenDates(budgetRequest)).thenReturn(0);

        BudgetForGivenDatesNotFound exception = assertThrows(
                BudgetForGivenDatesNotFound.class,
                () -> budgetController.deleteBudget(budgetRequest)
        );

        assertEquals("Budget for given dates not found",exception.getMessage());
        Mockito.verify(budgetService, Mockito.times(1)).deleteBudgetForGivenDates(budgetRequest);
    }
}
