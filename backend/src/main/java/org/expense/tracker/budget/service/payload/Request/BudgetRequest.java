package org.expense.tracker.budget.service.payload.Request;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequest {
    private BigInteger userId;
    @NotNull
    @Valid
    private String username;
//    @NotNull
//    @Valid
    private LocalDate startDate;
//    @NotNull
//    @Valid
    private LocalDate endDate;

    private LocalDateTime createdAt;

    @NotNull
    private BigDecimal allocatedAmount;

}
