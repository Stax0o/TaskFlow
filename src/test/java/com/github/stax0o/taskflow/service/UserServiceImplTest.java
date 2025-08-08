package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.UserDTO;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.exception.custom.BadRequestException;
import com.github.stax0o.taskflow.exception.custom.UserNotFoundException;
import com.github.stax0o.taskflow.mapper.UserMapper;
import com.github.stax0o.taskflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private static final String USERNAME = "name";
    private static final String EMAIL = "email@gmail.com";

    @Test
    void create() {
        UserDTO inputUserDTO = new UserDTO(USERNAME, EMAIL);
        User user = userBuilder(USERNAME, EMAIL);
        User savedUser = userBuilder(USERNAME, EMAIL);
        UserDTO expectedUserDTO = new UserDTO(USERNAME, EMAIL);

        when(userRepository.existsByUsername(inputUserDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(inputUserDTO.email())).thenReturn(false);
        when(userMapper.toEntity(inputUserDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedUserDTO);

        UserDTO result = userServiceImpl.create(inputUserDTO);

        assertEquals(expectedUserDTO, result);

        verify(userRepository).existsByUsername(inputUserDTO.username());
        verify(userRepository).existsByEmail(inputUserDTO.email());
        verify(userMapper).toEntity(inputUserDTO);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void create_shouldThrowException_whenUsernameAlreadyExists() {
        UserDTO inputUserDTO = new UserDTO(USERNAME, EMAIL);

        when(userRepository.existsByUsername(inputUserDTO.username())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userServiceImpl.create(inputUserDTO));

        verify(userRepository).existsByUsername(inputUserDTO.username());
    }

    @Test
    void create_shouldThrowException_whenEmailAlreadyExists() {
        UserDTO inputUserDTO = new UserDTO(USERNAME, EMAIL);

        when(userRepository.existsByUsername(inputUserDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(inputUserDTO.email())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userServiceImpl.create(inputUserDTO));

        verify(userRepository).existsByUsername(inputUserDTO.username());
        verify(userRepository).existsByEmail(inputUserDTO.email());
    }

    @Test
    void getByUsername() {
        User user = userBuilder(USERNAME, EMAIL);
        UserDTO userDTO = new UserDTO(USERNAME, EMAIL);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userServiceImpl.getByUsername(USERNAME);

        assertEquals(userDTO, result);
    }

    @Test
    void getByUsername_shouldThrowException_whenUsernameAlreadyExists() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getByUsername(USERNAME));

        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    void update() {
        String oldUsername = "old username";
        UserDTO inputUserDTO = new UserDTO(USERNAME, EMAIL);
        User user = userBuilder(oldUsername, EMAIL);
        User savedUser = userBuilder(USERNAME, EMAIL);
        UserDTO expectedUserDTO = new UserDTO(USERNAME, EMAIL);

        when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(inputUserDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(inputUserDTO.email())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedUserDTO);

        UserDTO result = userServiceImpl.update(oldUsername, inputUserDTO);

        assertEquals(expectedUserDTO, result);

        verify(userRepository).findByUsername(oldUsername);
        verify(userRepository).existsByUsername(inputUserDTO.username());
        verify(userRepository).existsByEmail(inputUserDTO.email());
        verify(userRepository).save(user);
        verify(userMapper).toDTO(savedUser);
    }

    @Test
    void update_shouldThrowException_whenUsernameAlreadyExists() {
        String oldUsername = "old username";
        UserDTO inputUserDTO = new UserDTO(USERNAME, EMAIL);
        User user = userBuilder(oldUsername, EMAIL);

        when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(inputUserDTO.username())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userServiceImpl.update(oldUsername, inputUserDTO));

        verify(userRepository).findByUsername(oldUsername);
        verify(userRepository).existsByUsername(inputUserDTO.username());
    }

    @Test
    void update_shouldThrowException_whenEmailAlreadyExists() {
        String oldEmail = "old email";
        UserDTO inputUserDTO = new UserDTO(USERNAME, EMAIL);
        User user = userBuilder(USERNAME, oldEmail);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(inputUserDTO.username())).thenReturn(false);
        when(userRepository.existsByEmail(inputUserDTO.email())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userServiceImpl.update(USERNAME, inputUserDTO));

        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository).existsByUsername(inputUserDTO.username());
        verify(userRepository).existsByEmail(inputUserDTO.email());
    }

    private User userBuilder(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }
}