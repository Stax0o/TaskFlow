package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.enums.Status;
import com.github.stax0o.taskflow.exception.custom.TaskNotFoundException;
import com.github.stax0o.taskflow.exception.custom.UserNotFoundException;
import com.github.stax0o.taskflow.mapper.TaskMapper;
import com.github.stax0o.taskflow.repository.TaskRepository;
import com.github.stax0o.taskflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    private static final String TITLE = "Task name";
    private static final String DESCRIPTION = "Description";
    private static final Status STATUS = Status.TODO;
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(3);

    @Test
    void create() {
        Long taskId = 1L;
        Long userId = 5L;

        User user = new User();
        user.setId(userId);

        TaskDTO inputTaskDTO = new TaskDTO(
                null,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        Task taskEntity = taskBuilder(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                user
        );

        Task savedTask = taskBuilder(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                user
        );

        TaskDTO expectedTaskDTO = new TaskDTO(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        when(userRepository.existsById(userId)).thenReturn(true);
        when(taskMapper.toEntity(inputTaskDTO)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(savedTask);
        when(taskMapper.toDTO(savedTask)).thenReturn(expectedTaskDTO);

        TaskDTO result = taskServiceImpl.create(inputTaskDTO);

        assertEquals(expectedTaskDTO, result);

        verify(userRepository).existsById(userId);
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

    @Test
    void create_shouldThrowException_whenUserNotFound() {
        Long userId = 999L;
        TaskDTO taskDTO = new TaskDTO(
                null,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> taskServiceImpl.create(taskDTO));

        verify(userRepository).existsById(userId);
    }

    @Test
    void getTaskById() {
        Long taskId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Task task = taskBuilder(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                user
        );

        TaskDTO taskDTO = new TaskDTO(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        when(taskRepository.getTasksById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskServiceImpl.getTaskById(taskId);
        assertEquals(taskDTO, result);

        verify(taskRepository).getTasksById(taskId);
        verify(taskMapper).toDTO(task);
    }

    @Test
    void getTaskById_shouldThrowException_whenTaskNotFound() {
        Long taskId = 999L;

        when(taskRepository.getTasksById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.getTaskById(taskId));

        verify(taskRepository).getTasksById(taskId);
    }


    @Test
    void update() {
        Long taskId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        TaskDTO inputTaskDTO = new TaskDTO(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        Task task = taskBuilder(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                user
        );

        Task updatedTask = taskBuilder(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                user
        );

        TaskDTO expectedTaskDTO = new TaskDTO(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        when(taskRepository.getTasksById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        when(taskMapper.toDTO(updatedTask)).thenReturn(expectedTaskDTO);

        TaskDTO result = taskServiceImpl.update(taskId, inputTaskDTO);

        assertEquals(expectedTaskDTO, result);

        verify(taskRepository).getTasksById(taskId);
        verify(userRepository).findById(userId);
        verify(taskRepository).save(task);
        verify(taskMapper).toDTO(updatedTask);
    }

    @Test
    void update_shouldThrowException_whenTaskNotFound() {
        Long taskId = 999L;
        Long userId = 1L;

        TaskDTO inputTaskDTO = new TaskDTO(
                null,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                userId
        );

        when(taskRepository.getTasksById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.update(taskId, inputTaskDTO));

        verify(taskRepository).getTasksById(taskId);
    }

    @Test
    void update_shouldThrowException_whenUserNotFound() {
        Long taskId = 1L;
        Long userId = 1L;
        Long nonExistentUserId = 999L;

        User user = new User();
        user.setId(userId);

        Task task = taskBuilder(
                taskId,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                user
        );

        TaskDTO inputTaskDTO = new TaskDTO(
                null,
                TITLE,
                DESCRIPTION,
                STATUS,
                DEADLINE,
                nonExistentUserId
        );

        when(taskRepository.getTasksById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskServiceImpl.update(taskId, inputTaskDTO));

        verify(taskRepository).getTasksById(taskId);
        verify(userRepository).findById(nonExistentUserId);
    }
}