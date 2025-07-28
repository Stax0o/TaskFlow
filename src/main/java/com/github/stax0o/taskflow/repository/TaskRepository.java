package com.github.stax0o.taskflow.repository;

import com.github.stax0o.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
