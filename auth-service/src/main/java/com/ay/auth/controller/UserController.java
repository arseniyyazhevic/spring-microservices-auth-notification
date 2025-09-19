package com.ay.auth.controller;

import com.ay.auth.dto.UserDto;
import com.ay.auth.entity.User;
import com.ay.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        log.info("Fetched all users, count: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Fetching user by id: {}", id);

        return userService.getUserById(id)
                .map(this::convertToDto)
                .map(userDto -> {
                    log.info("User found: {}", userDto.getUsername());
                    return ResponseEntity.ok(userDto);
                })
                .orElseGet(() -> {
                    log.warn("User with id {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Creating new user: {}", userDto.getUsername());

        if (userService.getUserByUsername(userDto.getUsername()).isPresent()) {
            log.warn("Cannot create user, username {} already exists", userDto.getUsername());
            return ResponseEntity.status(409).build(); // Conflict
        }

        User user = convertToEntity(userDto);
        User created = userService.createUser(user);
        log.info("User {} created successfully", created.getUsername());

        return ResponseEntity.status(201).body(convertToDto(created)); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("Updating user id {}: {}", id, userDto.getUsername());

        return userService.getUserById(id)
                .map(existing -> {
                    User updated = userService.updateUser(id, convertToEntity(userDto));
                    log.info("User {} updated successfully", updated.getUsername());
                    return ResponseEntity.ok(convertToDto(updated));
                })
                .orElseGet(() -> {
                    log.warn("User with id {} not found for update", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user id: {}", id);

        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    log.info("User id {} deleted successfully", id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> {
                    log.warn("User with id {} not found for deletion", id);
                    return ResponseEntity.notFound().<Void>build();
                });
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        return user;
    }
}
