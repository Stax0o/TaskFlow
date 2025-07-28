package com.github.stax0o.taskflow.mapper;

import com.github.stax0o.taskflow.dto.TaskDTO;
import com.github.stax0o.taskflow.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "user.id", target = "userId")
    TaskDTO toDTO(Task task);

    @Mapping(source = "userId", target = "user.id")
    Task toEntity(TaskDTO taskDTO);

    List<TaskDTO> toDTOList(List<Task> taskList);

    List<Task> toEntityList(List<TaskDTO> taskDTOList);
}
