package com.internalworking.knowledgebase.dto.auth;

import com.internalworking.knowledgebase.model.Role;

public record UserResponse(
        String id,
        String name,
        String email,
        Role role
) {
}

