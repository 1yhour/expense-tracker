package com.testing.expense_tracker.service.impl;

import com.testing.expense_tracker.dto.request.LoginRequest;
import com.testing.expense_tracker.dto.request.RegisterRequest;
import com.testing.expense_tracker.dto.response.AuthResponse;
import com.testing.expense_tracker.dto.response.RegisterResponse;
import com.testing.expense_tracker.entity.User;
import com.testing.expense_tracker.repository.UserRepository;
import com.testing.expense_tracker.security.JwtService;
import com.testing.expense_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        UserDetails user = userDetailsService.loadUserByUsername(request.email());

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = new User();
        user.setName(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new RegisterResponse("User registered successfully");
    }


}
