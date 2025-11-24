package com.tripto.booking_app.service.auth.impl;

import com.tripto.booking_app.dto.AuthResponse;
import com.tripto.booking_app.dto.LoginRequest;
import com.tripto.booking_app.dto.RegisterRequest;
import com.tripto.booking_app.entity.Role;
import com.tripto.booking_app.entity.User;
import com.tripto.booking_app.entity.UserRole;
import com.tripto.booking_app.mapper.RoleMapper;
import com.tripto.booking_app.mapper.UserMapper;
import com.tripto.booking_app.service.auth.AuthService;
import com.tripto.booking_app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) throws Exception {
        log.info("Login attempt for user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        log.info("User {} authenticated successfully", request.getUsername());

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(userDetails);

        log.info("JWT token generated for user: {}", request.getUsername());

        User user = userMapper.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public String register(RegisterRequest request) throws Exception {
        log.info("Registration attempt for username: {}", request.getUsername());

        if (userMapper.existsByUsername(request.getUsername())) {
            log.warn("Username already exists: {}", request.getUsername());
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userMapper.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .isActive(true)
                .build();

        Role userRole = roleMapper.findByName("USER")
                        .orElseThrow(() -> new RuntimeException("Error: Role ROLE_USER not found"));

        user.addRole(userRole);

        userMapper.save(user);
        roleMapper.saveUserRole(
                UserRole.builder()
                        .roleId(userRole.getId())
                        .userId(user.getId())
                        .build()
        );

        log.info("User registered successfully: {}", user.getUsername());

        return "User registered successfully!";
    }

    @Override
    public String refreshToken(String oldToken) throws Exception {
        log.info("Refreshing token");

        String username = jwtUtil.extractUsername(oldToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.validateToken(oldToken, userDetails)) {
            String newToken = jwtUtil.generateToken(userDetails);
            log.info("Token refreshed for user: {}", username);
            return newToken;
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    @Override
    public boolean validateToken(String token) throws Exception {
        try {
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
