package com.testing.expense_tracker.controller;

import com.testing.expense_tracker.dto.request.RegisterRequest;
import com.testing.expense_tracker.dto.response.AuthResponse;
import com.testing.expense_tracker.dto.request.LoginRequest;
import com.testing.expense_tracker.dto.response.RegisterResponse;
import com.testing.expense_tracker.security.JwtService;
import com.testing.expense_tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;



    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }
}
