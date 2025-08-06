package com.github.stax0o.taskflow.exception.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class BuildApiError {
    public static ApiError buildApiError(
            String type,
            HttpStatus status,
            String message,
            HttpServletRequest req) {
        return new ApiError(
                type,
                status.name(),
                status.value(),
                message,
                req.getRequestURI(),
                LocalDateTime.now()
        );
    }
}
