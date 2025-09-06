package com.github.stax0o.taskflow.security.auth;

import com.github.stax0o.taskflow.security.dto.LoginRequestDTO;
import com.github.stax0o.taskflow.security.dto.LoginResponseDTO;
import com.github.stax0o.taskflow.security.dto.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        authService.register(requestDTO);
        return ResponseEntity.ok("User register successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        String result = authService.login(requestDTO);
        return ResponseEntity.ok(new LoginResponseDTO(result));
    }
}
