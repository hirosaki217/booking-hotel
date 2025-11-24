package com.tripto.booking_app.service.user.impl;

import com.tripto.booking_app.entity.User;
import com.tripto.booking_app.mapper.UserMapper;
import com.tripto.booking_app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;


    @Override
    public Optional<User> findByUsername(String username) throws Exception {
        return userMapper.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) throws Exception {
        return Optional.empty();
    }

    @Override
    public Boolean existsByUsername(String username) throws Exception {
        return null;
    }

    @Override
    public Boolean existsByEmail(String email) throws Exception{
        return null;
    }
}
