package com.github.stax0o.taskflow.exception;

import com.github.stax0o.taskflow.exception.custom.BadRequestException;
import com.github.stax0o.taskflow.exception.custom.TaskNotFoundException;
import com.github.stax0o.taskflow.exception.custom.UserNotFoundException;
import com.github.stax0o.taskflow.exception.dto.ApiError;
import com.github.stax0o.taskflow.exception.dto.BuildApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex, HttpServletRequest req) {
        log.warn("Task not found: {}", ex.getMessage());

        ApiError error = BuildApiError.buildApiError(
                "task-not-found",
                HttpStatus.NOT_FOUND,
                "Something went wrong.",
                req
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest req) {
        log.warn("User not found: {}", ex.getMessage());

        ApiError error = BuildApiError.buildApiError(
                "user-not-found",
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                req
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, HttpServletRequest req) {
        log.warn("Bad request: {}", ex.getMessage());

        ApiError error = BuildApiError.buildApiError(
                "bad-request",
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                req
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        log.warn("Validation failed: {}", ex.getMessage());
        ApiError error = BuildApiError.buildApiError(
                "validation-failed",
                HttpStatus.BAD_REQUEST,
                "Нарушение ограничений данных",
                req
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        log.warn("Validation failed: {}", ex.getMessage());
        ApiError error = BuildApiError.buildApiError(
                "validation-failed",
                HttpStatus.BAD_REQUEST,
                "Нарушение ограничений данных",
                req
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception: {}", ex.getMessage());

        ApiError error = BuildApiError.buildApiError(
                "internal-error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера",
                req
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
