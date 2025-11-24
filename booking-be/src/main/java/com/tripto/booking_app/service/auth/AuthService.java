package com.tripto.booking_app.service.auth;

import com.tripto.booking_app.dto.AuthResponse;
import com.tripto.booking_app.dto.LoginRequest;
import com.tripto.booking_app.dto.RegisterRequest;

public interface AuthService {
    public AuthResponse login(LoginRequest request) throws Exception;
    public String register(RegisterRequest request) throws Exception;
    public String refreshToken(String oldToken) throws Exception;
    public boolean validateToken(String token) throws Exception;
}
