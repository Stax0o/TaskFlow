package com.github.stax0o.taskflow.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stax0o.taskflow.dto.UserDTO;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.integration.AbstractIntegrationTest;
import com.github.stax0o.taskflow.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/users - создание пользователя")
    void create() throws Exception {
        UserDTO userDTO = createUserDTO();

        long usersCount = userRepository.count();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userDTO.username()))
                .andExpect(jsonPath("$.email").value(userDTO.email()));

        assertEquals(usersCount + 1, userRepository.count());
    }

    @Test
    @DisplayName("GET /api/users/{username} - поиск пользователя")
    void findByUsername() throws Exception {
        User user = createUser();

        mockMvc.perform(get("/api/users/" + user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("PUT /api/users/{username} - обновление пользователя")
    void update() throws Exception {
        User user = createUser();
        UserDTO newUserDTO = createUserDTO();

        long usersCount = userRepository.count();

        mockMvc.perform(put("/api/users/" + user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(newUserDTO.username()))
                .andExpect(jsonPath("$.email").value(newUserDTO.email()));

        assertEquals(usersCount, userRepository.count());
    }

    @Test
    @DisplayName("DELETE /api/users/{username} удаление пользователя")
    void delete() throws Exception {
        User user = createUser();

        long usersCount = userRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + user.getUsername()))
                .andExpect(status().isNoContent());

        assertEquals(usersCount - 1, userRepository.count());

    }

    private UserDTO createUserDTO() {
        return new UserDTO(
                "username" + UUID.randomUUID(),
                "email+" + UUID.randomUUID() + "@gmail.com"
        );
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test+" + UUID.randomUUID() + "@gmail.com");
        user.setUsername("test+" + UUID.randomUUID());
        return userRepository.save(user);
    }
}
