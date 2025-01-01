package org.expense.tracker.budget.service.controller;

import org.expense.tracker.budget.service.Exception.Exceptionclasses.BudgetForGivenDatesNotFound;
import org.expense.tracker.budget.service.model.Budget;
import org.expense.tracker.budget.service.payload.Request.BudgetRequest;
import org.expense.tracker.budget.service.payload.Response.BudgetResponse;
import org.expense.tracker.budget.service.service.BudgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/budget")
public class BudgetController {
    private BudgetService budgetService;
    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

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

    @DeleteMapping("/delete-budget")
    public ResponseEntity<BudgetResponse> deleteBudget(@Validated @RequestBody BudgetRequest budgetRequest){
        int countDeletedRecords = 0;
        try {
           countDeletedRecords =  budgetService.deleteBudgetForGivenDates(budgetRequest);
        }catch (Exception  e){
            logger.error(e.getMessage());
        }
        if(countDeletedRecords == 0){
            throw new BudgetForGivenDatesNotFound("Budget for given dates not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(new BudgetResponse("Budget deleted successfully for the user: "+budgetRequest.getUsername()));
    }

}
