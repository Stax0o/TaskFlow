package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.UserDTO;

public interface UserService {
    UserDTO create(UserDTO userDTO);

    UserDTO getByUsername(String username);

    UserDTO update(String username, UserDTO userDTO);

    void delete(String username);

}
