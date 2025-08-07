package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.UserDTO;
import com.github.stax0o.taskflow.entity.User;
import com.github.stax0o.taskflow.exception.custom.BadRequestException;
import com.github.stax0o.taskflow.exception.custom.UserNotFoundException;
import com.github.stax0o.taskflow.mapper.UserMapper;
import com.github.stax0o.taskflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDTO create(UserDTO userDTO) {
        log.debug("Создание пользователя: username={}", userDTO.username());
        if (userRepository.existsByUsername(userDTO.username()) || userRepository.existsByEmail(userDTO.email())) {
            throw new BadRequestException(String.format("Username или email уже занят: username={%s}, email={%s}", userDTO.username(), userDTO.email()));
        }
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        log.debug("Пользователь создан: username={}", userDTO.username());
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = getUserByUsername(username);
        log.debug("Пользователь найден: username={}", username);
        return userMapper.toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO update(String username, UserDTO userDTO) {
        User user = getUserByUsername(username);
        checkUsernameAvailable(user, userDTO.username());
        checkEmailAvailable(user, userDTO.email());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        User savedUser = userRepository.save(user);
        log.debug("Данные пользователя обновлены: new username={}", user.getUsername());
        return userMapper.toDTO(savedUser);
    }

    @Override
    public void delete(String username) {
        User user = getUserByUsername(username);
        userRepository.delete(user);
        log.debug("Пользователь удален: username={}", username);
    }

    private User getUserByUsername(String username) {
        log.debug("Загрузка пользователя по username={}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private void checkUsernameAvailable(User user, String username) {
        if (userRepository.existsByUsername(username) && !user.getUsername().equals(username)) {
            throw new BadRequestException(String.format("Username уже занят: username={%s}", username));
        }
    }

    private void checkEmailAvailable(User user, String email) {
        if (userRepository.existsByEmail(email) && !user.getEmail().equals(email)) {
            throw new BadRequestException(String.format("Email уже занят: email={%s}", email));
        }
    }
}
