package com.github.stax0o.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @Email
        @NotBlank(message = "Email не должен быть пустым")
        @Size(max = 255, message = "Email не должен превышать 255 символов")
        String email,

        @NotBlank
        String password
) {
}
