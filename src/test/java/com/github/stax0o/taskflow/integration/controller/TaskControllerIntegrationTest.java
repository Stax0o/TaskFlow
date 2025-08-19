package com.github.stax0o.taskflow.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.enums.Status;
import com.github.stax0o.taskflow.integration.AbstractIntegrationTest;
import com.github.stax0o.taskflow.repository.TaskRepository;
import com.github.stax0o.taskflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

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
    static void createUserAndTask(
            @Autowired UserRepository userRepository,
            @Autowired TaskRepository taskRepository
    ) {
        User user = new User();
        user.setEmail("test+" + UUID.randomUUID() + "@gmail.com");
        user.setUsername("test+" + UUID.randomUUID());
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
        long taskCount = taskRepository.count();
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(simpleTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(simpleTask.title()))
                .andExpect(jsonPath("$.description").value(simpleTask.description()));

        assertEquals(taskCount + 1, taskRepository.count());
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - запрос задачи")
    void getTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value(simpleTask.title()))
                .andExpect(jsonPath("$.description").value(simpleTask.description()))
                .andExpect(jsonPath("$.status").value(simpleTask.status().name()));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} - обновление задачи")
    void update() throws Exception {
        User user = createUser();

        Task task = createTask(user);

        long taskCount = taskRepository.count();

        long newUserId = 1L;

        TaskDTO newTaskDTO = new TaskDTO(null, "New title", "New description", Status.IN_PROGRESS, LocalDateTime.now().plusDays(5), newUserId);

        mockMvc.perform(put("/api/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value(newTaskDTO.title()))
                .andExpect(jsonPath("$.description").value(newTaskDTO.description()))
                .andExpect(jsonPath("$.status").value(newTaskDTO.status().name()))
                .andExpect(jsonPath("$.userId").value(newUserId));

        assertEquals(taskCount, taskRepository.count());
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - удаление задачи")
    void delete() throws Exception {
        Task task = createTask(createUser());

        long taskCount = taskRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + task.getId()))
                .andExpect(status().isNoContent());

        assertEquals(taskCount - 1, taskRepository.count());
    }

    @Test
    @DisplayName("GET /api/tasks/{username} - получение задач пользователя")
    void getTasksByUsername() throws Exception {
        User user = createUser();
        Task task1 = createTask(user);
        Task task2 = createTask(user);

        mockMvc.perform(get("/api/tasks/user_tasks/" + user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(task1.getId()))
                .andExpect(jsonPath("$[0].title").value(task1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(task1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(task2.getId()))
                .andExpect(jsonPath("$[1].title").value(task2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(task2.getDescription()));
    }

    private User createUser() {
        User user = new User();
        user.setEmail("test+" + UUID.randomUUID() + "@gmail.com");
        user.setUsername("test+" + UUID.randomUUID());
        return userRepository.save(user);
    }

    private Task createTask(User user) {
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Description");
        task.setStatus(Status.TODO);
        task.setDeadline(LocalDateTime.now().plusDays(3));
        task.setUser(user);
        return taskRepository.save(task);
    }
}
