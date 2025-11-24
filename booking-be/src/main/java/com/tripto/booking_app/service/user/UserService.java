package com.tripto.booking_app.service.user;

import com.tripto.booking_app.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username) throws Exception;
    Optional<User> findByEmail(String email) throws Exception;
    Boolean existsByUsername(String username) throws Exception;
    Boolean existsByEmail(String email) throws Exception;
}
