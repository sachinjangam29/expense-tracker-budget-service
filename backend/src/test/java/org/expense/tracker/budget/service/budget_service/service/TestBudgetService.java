package org.expense.tracker.budget.service.budget_service.service;

import org.expense.tracker.budget.service.Exception.Exceptionclasses.BudgetForGivenDatesAlreadyCreatedForUser;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.BudgetForGivenDatesNotFound;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.UserNotFoundException;
import org.expense.tracker.budget.service.Exception.Exceptionclasses.ValidDateException;
import org.expense.tracker.budget.service.UserClient;
import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.repository.BudgetRepository;
import org.expense.tracker.budget.service.service.BudgetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private BudgetService budgetService;

    private BudgetRequest budgetRequest;

    @BeforeEach
    void setUp() {
        budgetRequest = new BudgetRequest();
        budgetRequest.setUsername("testUser");
        budgetRequest.setStartDate(LocalDate.of(2024, 1, 1));
        budgetRequest.setEndDate(LocalDate.of(2024, 1, 31));
        budgetRequest.setAllocatedAmount(BigDecimal.valueOf(1000));
    }

    @Test
    void testAddBudget_Success() {
        // Arrange
        BigInteger userId = BigInteger.valueOf(1);
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setStartDate(budgetRequest.getStartDate());
        budget.setEndDate(budgetRequest.getEndDate());
        budget.setAllocatedAmount(budgetRequest.getAllocatedAmount());

        Mockito.when(userClient.getUserId(budgetRequest.getUsername())).thenReturn(userId);
        Mockito.when(budgetRepository.checkValidStartDate(budgetRequest.getStartDate(), userId)).thenReturn(false);
        Mockito.when(budgetRepository.existsByOverlappingDates(budgetRequest.getStartDate(), budgetRequest.getEndDate(), userId)).thenReturn(false);
        Mockito.when(budgetRepository.save(Mockito.any(Budget.class))).thenReturn(budget);

        // Act
        Budget result = budgetService.addBudget(budgetRequest);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.getUserId());
        Assertions.assertEquals(budgetRequest.getStartDate(), result.getStartDate());
        Assertions.assertEquals(budgetRequest.getEndDate(), result.getEndDate());
        Assertions.assertEquals(budgetRequest.getAllocatedAmount(), result.getAllocatedAmount());
        Mockito.verify(budgetRepository, Mockito.times(1)).save(Mockito.any(Budget.class));
    }

    @Test
    void testAddBudget_EndDateBeforeStartDate() {
        Mockito.when(userClient.getUserId(budgetRequest.getUsername())).thenReturn(BigInteger.valueOf(1));
        // Arrange
        budgetRequest.setEndDate(LocalDate.of(2024, 11, 20));
        budgetRequest.setStartDate(LocalDate.of(2024, 12, 1));
        budgetRequest.setUsername("testUser");

        // Act & Assert
        ValidDateException exception = Assertions.assertThrows(
                ValidDateException.class,
                () -> budgetService.addBudget(budgetRequest)
        );

        Assertions.assertEquals("End date cannot be before start date. Please provide valid dates.", exception.getMessage());
    }

    @Test
    void testAddBudget_OverlappingDates() {
        // Arrange
        BigInteger userId = BigInteger.valueOf(1);
        Mockito.when(userClient.getUserId(budgetRequest.getUsername())).thenReturn(userId);
        Mockito.when(budgetRepository.existsByOverlappingDates(budgetRequest.getStartDate(), budgetRequest.getEndDate(), userId)).thenReturn(true);

        // Act & Assert
        BudgetForGivenDatesAlreadyCreatedForUser exception = Assertions.assertThrows(
                BudgetForGivenDatesAlreadyCreatedForUser.class,
                () -> budgetService.addBudget(budgetRequest)
        );

        Assertions.assertEquals(
                "Budget already exists for the given date range: 2024-01-01 to 2024-01-31",
                exception.getMessage()
        );
    }

    @Test
    void testAddBudget_UserNotFound() {
        // Arrange
        Mockito.when(userClient.getUserId(budgetRequest.getUsername())).thenReturn(null);

        // Act & Assert
        UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> budgetService.addBudget(budgetRequest)
        );

        Assertions.assertEquals("Username not available: {} testUser", exception.getMessage());
    }

    @Test
    void testDeleteBudget_Success() {
        // Arrange
        BigInteger userId = BigInteger.valueOf(1);
        Mockito.when(userClient.getUserId(budgetRequest.getUsername())).thenReturn(userId);
        Mockito.when(budgetRepository.deleteByStartAndEndDate(budgetRequest.getStartDate(), budgetRequest.getEndDate(), userId)).thenReturn(1);

        // Act
        int result = budgetService.deleteBudgetForGivenDates(budgetRequest);

        // Assert
        Assertions.assertEquals(1, result);
        Mockito.verify(budgetRepository, Mockito.times(1)).deleteByStartAndEndDate(budgetRequest.getStartDate(), budgetRequest.getEndDate(), userId);
    }

    @Test
    void testDeleteBudget_NotFound() {
        // Arrange
        BigInteger userId = BigInteger.valueOf(1);
        BudgetRequest budgetRequest = new BudgetRequest();
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 21);
        budgetRequest.setStartDate(startDate);
        budgetRequest.setEndDate(endDate);
        budgetRequest.setUsername("testUser");
//        budgetRequest.setUsername("testUser");

        Mockito.when(userClient.getUserId("testUser")).thenReturn(userId);
        Mockito.when(budgetRepository.deleteByStartAndEndDate(startDate, endDate, userId))
                .thenReturn(0);

        // Act & Assert
        BudgetForGivenDatesNotFound exception = Assertions.assertThrows(
                BudgetForGivenDatesNotFound.class,
                () -> budgetService.deleteBudgetForGivenDates(budgetRequest)
        );

        Assertions.assertEquals("Budgets for the given dates not found", exception.getMessage());
    }
}
