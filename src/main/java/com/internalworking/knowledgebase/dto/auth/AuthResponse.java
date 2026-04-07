package com.internalworking.knowledgebase.dto.auth;

public record AuthResponse(
        String token,
        UserResponse user
) {
}

