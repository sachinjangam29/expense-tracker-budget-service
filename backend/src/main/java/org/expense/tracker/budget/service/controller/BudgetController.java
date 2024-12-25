package org.expense.tracker.budget.service.controller;

import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.payload.Response.BudgetResponse;
import org.expense.tracker.budget.service.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/v1/budget")
public class BudgetController {
    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService){
        this.budgetService = budgetService;
    }

    @GetMapping("/hello")
    public String printHelloBudget(){
        return "Hello Budget";
    }

    @PostMapping("/create")
    public ResponseEntity<BudgetResponse> addBudgetEntry(@Validated @RequestBody BudgetRequest budgetRequest){
        Budget budget = budgetService.addBudget(budgetRequest);
        BigDecimal calculatdCarryForward;
        if (budget.getCarryForward() == null){
            calculatdCarryForward = BigDecimal.ZERO;
        } else{
            calculatdCarryForward = budget.getCarryForward();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new BudgetResponse(budget.getUserId(),
                budget.getStartDate(),
                budget.getEndDate(),
                budget.getAllocatedAmount(),
                calculatdCarryForward,
                "Budget saved successfully..."));
    }

}
