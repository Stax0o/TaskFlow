package com.github.stax0o.taskflow.service;

import com.github.stax0o.taskflow.dto.UserDTO;
import com.github.stax0o.taskflow.entity.User;
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
        log.info("Создание пользователя: username={}", userDTO.username());
        checkUsernameAvailable(userDTO.username());
        checkEmailAvailable(userDTO.email());
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        log.info("Пользователь создан: username={}", userDTO.username());
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = getUserByUsername(username);
        log.info("Пользователь найден: username={}", username);
        return userMapper.toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO update(String username, UserDTO userDTO) {
        User user = getUserByUsername(username);
        checkUsernameAvailable(userDTO.username());
        checkEmailAvailable(userDTO.email());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        User savedUser = userRepository.save(user);
        log.info("Данные пользователя обновлены: new username={}", user.getUsername());
        return userMapper.toDTO(savedUser);
    }

    @Override
    public void delete(String username) {
        User user = getUserByUsername(username);
        userRepository.delete(user);
        log.info("Пользователь удален: username={}", username);
    }

    private User getUserByUsername(String username) {
        log.info("Загрузка пользователя по username={}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: username={}", username);
//                    todo заменить на свое кастомное исключение NotFoundException и @RestControllerAdvice
                    return new EntityNotFoundException("Пользователь не найден: " + username);
                });
        return user;
    }

    private boolean checkUsernameAvailable(String username) {
        boolean result = userRepository.existsByUsername(username);
        log.warn("username уже занят: username={}", username);
//            todo Создать кастомное исключение BadRequestException
//            throw new BadRequestException("Username уже существует");
        return result;
    }

    private boolean checkEmailAvailable(String email) {
        boolean result = userRepository.existsByEmail(email);
        log.warn("email уже занят: email={}", email);
//            todo Создать кастомное исключение BadRequestException
//            throw new BadRequestException("Email уже существует");
        return result;
    }
}
