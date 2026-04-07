package com.internalworking.knowledgebase.security;

import com.internalworking.knowledgebase.exception.ResourceNotFoundException;
import com.internalworking.knowledgebase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username.toLowerCase())
                .map(AppUserDetails::new)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

