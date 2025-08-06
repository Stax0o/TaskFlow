package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.enums.Status;
import com.github.stax0o.taskflow.mapper.TaskMapper;
import com.github.stax0o.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Test
    void create() {
        Long userId = 5L;
        Long taskId = 1L;

        User user = new User();
        user.setId(userId);

        TaskDTO inputTaskDTO = new TaskDTO(
                null,
                "Task name",
                "Description",
                Status.TODO,
                LocalDateTime.now().plusDays(3),
                userId
        );

        Task taskEntity = taskBuilder(
                inputTaskDTO.id(),
                inputTaskDTO.title(),
                inputTaskDTO.description(),
                inputTaskDTO.status(),
                inputTaskDTO.deadline(),
                user
        );

        Task savedTask = taskBuilder(
                taskId,
                taskEntity.getTitle(),
                taskEntity.getDescription(),
                taskEntity.getStatus(),
                taskEntity.getDeadline(),
                taskEntity.getUser()
        );

        TaskDTO expectedTaskDTO = new TaskDTO(
                taskId,
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getDeadline(),
                savedTask.getUser().getId()
        );

        when(userServiceImpl.getUserById(userId)).thenReturn(user);
        when(taskMapper.toEntity(inputTaskDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(savedTask);
        when(taskMapper.toDTO(savedTask)).thenReturn(expectedTaskDTO);

        TaskDTO result = taskServiceImpl.create(inputTaskDTO);

        assertEquals(expectedTaskDTO, result);

        verify(userServiceImpl).getUserById(userId);
        verify(taskMapper).toEntity(inputTaskDTO);
        verify(taskRepository).save(taskEntity);
        verify(taskMapper).toDTO(savedTask);
    }

    private Task taskBuilder(Long id,
                             String title,
                             String description,
                             Status status,
                             LocalDateTime deadline,
                             User user) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setDeadline(deadline);
        task.setUser(user);

        return task;
    }
}