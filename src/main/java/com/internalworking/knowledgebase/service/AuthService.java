package com.internalworking.knowledgebase.service;

import com.internalworking.knowledgebase.dto.auth.AuthRequest;
import com.internalworking.knowledgebase.dto.auth.AuthResponse;
import com.internalworking.knowledgebase.dto.auth.RegisterRequest;
import com.internalworking.knowledgebase.dto.auth.UserResponse;
import com.internalworking.knowledgebase.exception.ConflictException;
import com.internalworking.knowledgebase.model.Role;
import com.internalworking.knowledgebase.model.User;
import com.internalworking.knowledgebase.repository.UserRepository;
import com.internalworking.knowledgebase.security.AppUserDetails;
import com.internalworking.knowledgebase.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email is already registered");
        }

        Role assignedRole = userRepository.count() == 0 ? Role.ADMIN : Role.USER;

        User user = userRepository.save(User.builder()
                .name(request.name().trim())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .role(assignedRole)
                .createdAt(Instant.now())
                .build());

        return createAuthResponse(user);
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email().trim().toLowerCase(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new ConflictException("User does not exist"));
        return createAuthResponse(user);
    }

    public UserResponse me(AppUserDetails userDetails) {
        return mapUser(userDetails.getId(), userDetails.getName(), userDetails.getUsername(), userDetails.getRole());
    }

    private AuthResponse createAuthResponse(User user) {
        AppUserDetails userDetails = new AppUserDetails(user);
        return new AuthResponse(jwtService.generateToken(userDetails), mapUser(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        ));
    }

    private UserResponse mapUser(String id, String name, String email, Role role) {
        return new UserResponse(id, name, email, role);
    }
}
