package com.github.stax0o.taskflow.controller;

import com.github.stax0o.taskflow.dto.UserDTO;
import com.github.stax0o.taskflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
//todo добавить логирование в UserController
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUser = userService.create(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username) {
        UserDTO userDTO = userService.getByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> update(@PathVariable String username, @RequestBody @Valid UserDTO userDTO) {
        UserDTO updateUser = userService.update(username, userDTO);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity
                .noContent()
                .build();
    }
}
