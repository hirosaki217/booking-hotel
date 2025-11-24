package com.tripto.booking_app.controller;

import com.tripto.booking_app.dto.*;
import com.tripto.booking_app.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Login request for user: {}", request.getUsername());

            AuthResponse response = authService.login(request);

            log.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user {}: {}",
                    request.getUsername(), e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid username or password"));
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Registration request for username: {}", request.getUsername());

            String message = authService.register(request);

            log.info("Registration successful for user: {}", request.getUsername());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponse(message));

        } catch (RuntimeException e) {
            log.error("Registration failed: {}", e.getMessage());

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token từ "Bearer {token}"
            String oldToken = authHeader.substring(7);

            String newToken = authService.refreshToken(oldToken);

            AuthResponse response = AuthResponse.builder()
                    .token(newToken)
                    .type("Bearer")
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid or expired token"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            boolean isValid = authService.validateToken(token);

            return ResponseEntity.ok()
                    .body(new ValidationResponse(isValid));

        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(new ValidationResponse(false));
        }
    }
}
