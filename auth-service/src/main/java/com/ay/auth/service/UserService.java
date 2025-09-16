package com.ay.auth.service;

import com.ay.auth.client.NotificationClient;
import com.ay.auth.entity.User;
import com.ay.auth.enums.Role;
import com.ay.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotificationClient notificationClient;

    public User createUser(User user) {
        User saved = userRepository.save(user);
        notifyAdmins("Создан пользователь " + user.getUsername(), saved);
        return saved;
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setRole(updatedUser.getRole());
        User saved = userRepository.save(user);
        notifyAdmins("Изменен пользователь " + user.getUsername(), saved);
        return saved;
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);
        notifyAdmins("Удален пользователь " + user.getUsername(), user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    private void notifyAdmins(String subject, User user) {
        List<User> admins = getAdmins();
        for (User admin : admins) {
            notificationClient.sendNotification(
                    subject,
                    "Пользователь с именем: " + user.getUsername() +
                            ", email: " + user.getEmail(),
                    admin.getEmail()
            );
        }
    }

    public List<User> getAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN)
                .toList();
    }
}
