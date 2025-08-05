package com.github.stax0o.taskflow.dto;

import com.github.stax0o.taskflow.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskDTO(
        Long id,

        @NotBlank(message = "Заголовок не должен быть пустым")
        @Size(max = 255, message = "Длинна заголовка не должна превышать 255 символов")
        String title,

        @Size(max = 1000, message = "Длинна описания не должна превышать 1000 символов")
        String description,

        Status status,

        LocalDateTime deadline,

        @NotNull(message = "Должен быть указан владелец задачи")
        Long userId
) {
}
