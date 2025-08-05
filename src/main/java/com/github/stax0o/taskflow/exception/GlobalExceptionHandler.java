package com.github.stax0o.taskflow.exception;

import com.github.stax0o.taskflow.exception.custom.TaskNotFoundException;
import com.github.stax0o.taskflow.exception.custom.UserNotFoundException;
import com.github.stax0o.taskflow.exception.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex, HttpServletRequest req) {
        log.error("Task not found: {}", ex.getMessage());

        ApiError error = new ApiError(
                "task-not-found",
                HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.value(),
                "Something went wrong.",
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest req) {
        log.error("User not found: {}", ex.getMessage());

        ApiError error = new ApiError(
                "user-not-found",
                HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.value(),
                "Something went wrong.",
                req.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
