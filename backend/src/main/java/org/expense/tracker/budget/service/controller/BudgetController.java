package org.expense.tracker.budget.service.controller;

import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.payload.Response.BudgetResponse;
import org.expense.tracker.budget.service.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<BudgetResponse> addBudgetEntry(@Validated @RequestBody BudgetRequest budgetRequest){
        Budget budget = budgetService.addBudget(budgetRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BudgetResponse("Budget saved successfully..."));
    }

}
