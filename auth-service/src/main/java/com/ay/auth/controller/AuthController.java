package com.ay.auth.controller;

import com.ay.auth.dto.auth.AuthRequest;
import com.ay.auth.dto.auth.AuthResponse;
import com.ay.auth.enums.Role;
import com.ay.auth.entity.User;
import com.ay.auth.service.UserService;
import com.ay.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        log.info("Attempting to register user: {}", request.getUsername());

        if (userService.getUserByUsername(request.getUsername()).isPresent()) {
            log.warn("Registration failed: username {} already exists", request.getUsername());
            return ResponseEntity.status(409).body(new AuthResponse("Username already exists"));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.USER);

        User saved = userService.createUser(user);
        log.info("User {} successfully registered", saved.getUsername());

        String token = jwtUtil.generateToken(saved.getUsername(), saved.getRole().name());

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("User {} attempting login", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user {}", request.getUsername());
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password"));
        }

        User user = userService.getUserByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("User {} not found after successful authentication", request.getUsername());
                    return new RuntimeException("User not found");
                });

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        log.info("User {} successfully logged in", user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

