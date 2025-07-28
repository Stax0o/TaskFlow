package com.github.stax0o.taskflow.repository;

import com.github.stax0o.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
