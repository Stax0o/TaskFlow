package com.github.stax0o.taskflow.exception.dto;

import java.time.LocalDateTime;

public record ApiError(
        String type,          // Тип ошибки (можно URL)
        String title,         // Краткое описание
        int status,           // HTTP статус
        String detail,        // Детали ошибки
        String instance,      // URI запроса
        LocalDateTime timestamp
) {
}
