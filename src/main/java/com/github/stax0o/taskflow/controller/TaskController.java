package com.github.stax0o.taskflow.controller;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
//todo добавить логирование в TaskController
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> create(@RequestBody @Valid TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.create(taskDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.update(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/user_tasks/{username}")
    public ResponseEntity<List<TaskDTO>> getTasksByUsername(@PathVariable String username) {
        List<TaskDTO> taskDTOList = taskService.getTasksByUsername(username);
        return ResponseEntity.ok(taskDTOList);
    }
}
