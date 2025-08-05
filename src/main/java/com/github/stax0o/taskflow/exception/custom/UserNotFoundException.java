package com.github.stax0o.taskflow.exception.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("User with id " + username + " not found");
    }

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
