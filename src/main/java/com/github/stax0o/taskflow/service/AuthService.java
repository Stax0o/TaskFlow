package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.LoginRequestDTO;
import com.github.stax0o.taskflow.dto.RegisterRequestDTO;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.enums.Role;
import com.github.stax0o.taskflow.exception.custom.BadRequestException;
import com.github.stax0o.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.email())) {
            throw new BadRequestException(String.format("Email уже занят: email={%s}", requestDTO.email()));
        }

        String encodedPassword = passwordEncoder.encode(requestDTO.password());

        User user = User.builder()
                .email(requestDTO.email())
                .username(requestDTO.username())
                .password(encodedPassword)
                .roles(Set.of(Role.ROLE_USER))
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequestDTO requestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDTO.email(),
                            requestDTO.password()
                    )
            );
        } catch (AuthenticationException e) {
            throw new BadRequestException(String.format("Введен неверный email или пароль: email={%s}", requestDTO.email()));
        }

        return "Login successful";
    }
}
