package org.expense.tracker.budget.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigInteger;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/auth/user")
public interface UserClient {
    @GetMapping("/user/{username}")
    public BigInteger getUserId(@Validated @PathVariable String username);
}
