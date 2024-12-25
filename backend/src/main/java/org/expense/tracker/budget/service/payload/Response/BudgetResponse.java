package org.expense.tracker.budget.service.payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResponse {
    private BigInteger userId;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal allocatedAmount;

    private BigDecimal carryForward;

    private String message;

    public BudgetResponse(String message){
       this.message = message;
    }
}
