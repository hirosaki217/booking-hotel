package com.tripto.booking_app.mapper;

import com.tripto.booking_app.entity.User;

import java.util.Optional;

public interface UserMapper {

    Optional<User> findByUsername(String username);
}
