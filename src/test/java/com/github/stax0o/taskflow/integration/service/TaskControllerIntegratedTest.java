package com.github.stax0o.taskflow.integration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.enums.Status;
import com.github.stax0o.taskflow.integration.AbstractIntegrationTest;
import com.github.stax0o.taskflow.repository.TaskRepository;
import com.github.stax0o.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerIntegratedTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final TaskDTO simpleTask = new TaskDTO(
            null,
            "Title",
            "Description",
            Status.TODO,
            LocalDateTime.now().plusDays(3),
            1L);

    @BeforeAll
    static void createUser(
            @Autowired UserRepository userRepository,
            @Autowired TaskRepository taskRepository
    ) {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@gmail.com");
        user = userRepository.save(user);

        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Description");
        task.setStatus(Status.TODO);
        task.setDeadline(LocalDateTime.now().plusDays(3));
        task.setUser(user);
        taskRepository.save(task);
    }

    @Test
    @DisplayName("POST /api/tasks - создание задачи")
    void create_shouldCreatedTask() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simpleTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value(simpleTask.title()))
                .andExpect(jsonPath("$.description").value(simpleTask.description()));

        assertEquals(2, taskRepository.count());
    }

    @Test
    @DisplayName("GET /api/tasks - Запрос задачи")
    void getTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(simpleTask.title()))
                .andExpect(jsonPath("$.description").value(simpleTask.description()))
                .andExpect(jsonPath("$.status").value(simpleTask.status().name()));
    }



}
