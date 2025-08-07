package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.exception.custom.TaskNotFoundException;
import com.github.stax0o.taskflow.exception.custom.UserNotFoundException;
import com.github.stax0o.taskflow.mapper.TaskMapper;
import com.github.stax0o.taskflow.repository.TaskRepository;
import com.github.stax0o.taskflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public TaskDTO create(TaskDTO taskDTO) {
        log.debug("Создание задачи: title={}", taskDTO.title());
        if (!userRepository.existsById(taskDTO.userId())) {
            throw new UserNotFoundException(taskDTO.userId());
        }
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        log.debug("Задача создана: title={}, id={}", savedTask.getTitle(), savedTask.getId());
        return taskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = getTaskByTaskId(id);
        log.debug("Задача загружена: id={}", id);
        return taskMapper.toDTO(task);
    }

    @Transactional
    @Override
    public TaskDTO update(Long id, TaskDTO taskDTO) {
        Task task = getTaskByTaskId(id);
        log.debug("Загрузка пользователя по id={}", taskDTO.userId());
        User user = userRepository.findById(taskDTO.userId())
                .orElseThrow(() -> new UserNotFoundException(taskDTO.userId()));
        task.setUser(user);
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setStatus(taskDTO.status());
        task.setDeadline(taskDTO.deadline());

        Task updatedTask = taskRepository.save(task);
        log.debug("Задача обновлена: id={}", updatedTask.getId());
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
        log.debug("Задача удалена: id={}", id);
    }

    @Transactional
    @Override
    public List<TaskDTO> getTasksByUsername(String username) {
        log.debug("Получение списка задач по username: username={}", username);
        List<Task> tasks = taskRepository.getAllByUserUsername(username);
        log.debug("Список задач по username получен: username={}", username);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    private Task getTaskByTaskId(Long id) {
        log.debug("Загрузка задачи по id={}", id);
        return taskRepository.getTasksById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
