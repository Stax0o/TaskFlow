package com.github.stax0o.taskflow.controller;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskDTO taskDTO) {
        log.info("POST /api/tasks | Создание задачи | title: {}", taskDTO.title());
        TaskDTO createdTask = taskService.create(taskDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        log.info("GET /api/tasks/{} | Запрос задачи", id);
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        log.info("PUT /api/tasks/{} | Обновление задачи | title: {}", id, taskDTO.title());
        TaskDTO updatedTask = taskService.update(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/tasks/{} | Удаление задачи", id);
        taskService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/user_tasks/{username}")
    public ResponseEntity<List<TaskDTO>> getTasksByUsername(@PathVariable String username) {
        log.info("GET /api/tasks/user_tasks/{} | Запрос задач пользователя", username);
        List<TaskDTO> taskDTOList = taskService.getTasksByUsername(username);
        return ResponseEntity.ok(taskDTOList);
    }
}
