package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import com.github.stax0o.taskflow.exception.custom.TaskNotFoundException;
import com.github.stax0o.taskflow.mapper.TaskMapper;
import com.github.stax0o.taskflow.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserServiceImpl userServiceImpl;

    @Transactional
    @Override
    public TaskDTO create(TaskDTO taskDTO) {
        log.info("Создание задачи: title={}", taskDTO.title());
        userServiceImpl.getUserById(taskDTO.userId());
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        log.info("Задача создана: title={}, id={}", savedTask.getTitle(), savedTask.getId());
        return taskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = getTaskByTaskId(id);
        log.info("Задача загружена: id={}", id);
        return taskMapper.toDTO(task);
    }

    @Transactional
    @Override
    public TaskDTO update(Long id, TaskDTO taskDTO) {
        Task task = getTaskByTaskId(id);
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setStatus(taskDTO.status());
        task.setDeadline(taskDTO.deadline());
        task.setUser(userServiceImpl.getUserById(taskDTO.id()));
        Task updatedTask = taskRepository.save(task);
        log.info("Задача обновлена: id={}", updatedTask.getId());
        return taskMapper.toDTO(updatedTask);
    }

    @Override
    public void delete(Long id) {
        Task task = getTaskByTaskId(id);
        taskRepository.delete(task);
        log.info("Задача удалена: id={}", id);
    }

    @Transactional
    @Override
    public List<TaskDTO> getTasksByUsername(String username) {
        log.info("Получение списка задач по username: username={}", username);
        List<Task> tasks = taskRepository.getAllByUserUsername(username);
        log.info("Список задач по username получен: username={}", username);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    private Task getTaskByTaskId(Long id) {
        log.info("Загрузка задачи по id={}", id);
        return taskRepository.getTasksById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
