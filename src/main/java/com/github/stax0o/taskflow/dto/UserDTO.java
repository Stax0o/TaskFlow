package com.github.stax0o.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "Имя пользователя не должно быть пустым")
        @Size(max = 255, message = "Имя пользователя не должно превышать 255 символов")
        String username,

        @NotBlank(message = "Email не должен быть пустым")
        @Size(max = 255, message = "Email не должен превышать 255 символов")
        String email) {
}
