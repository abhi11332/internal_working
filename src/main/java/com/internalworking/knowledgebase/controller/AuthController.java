package com.internalworking.knowledgebase.controller;

import com.internalworking.knowledgebase.dto.auth.AuthRequest;
import com.internalworking.knowledgebase.dto.auth.AuthResponse;
import com.internalworking.knowledgebase.dto.auth.RegisterRequest;
import com.internalworking.knowledgebase.dto.auth.UserResponse;
import com.internalworking.knowledgebase.security.AppUserDetails;
import com.internalworking.knowledgebase.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/auth", "/api/v1/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal AppUserDetails currentUser) {
        return authService.me(currentUser);
    }
}
