package com.testing.expense_tracker.service;

import com.testing.expense_tracker.dto.request.LoginRequest;
import com.testing.expense_tracker.dto.request.RegisterRequest;
import com.testing.expense_tracker.dto.response.AuthResponse;
import com.testing.expense_tracker.dto.response.RegisterResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
}
