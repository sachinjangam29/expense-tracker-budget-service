package org.expense.tracker.budget.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {

    @Id
    @Column(name = "budget_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger budget_Id;

    @Column(name = "user_id")
    private BigInteger userId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "allocated_amount")
    private BigDecimal allocatedAmount;

    @Column(name = "carry_forward")
    private BigDecimal carryForward;
}
