package org.expense.tracker.budget.service.Exception.ExceptionMessageFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionMessageFormat {
    private HttpStatus status;
    private String path;
    private String message;
    private LocalDateTime timestamp;
}
