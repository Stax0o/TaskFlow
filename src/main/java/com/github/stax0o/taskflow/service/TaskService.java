package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO create(TaskDTO taskDTO);

    TaskDTO getTaskById(Long id);

    TaskDTO update(Long id, TaskDTO taskDTO);

    void delete(Long id);

    List<TaskDTO> getTasksByUsername(String username);
}
