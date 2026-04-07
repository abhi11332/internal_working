package com.internalworking.knowledgebase;

import com.internalworking.knowledgebase.model.Role;
import com.internalworking.knowledgebase.model.User;
import com.internalworking.knowledgebase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@SpringBootApplication
public class KnowledgeBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowledgeBaseApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.name}") String adminName,
            @Value("${app.admin.email}") String adminEmail,
            @Value("${app.admin.password}") String adminPassword
    ) {
        return args -> {
            if (userRepository.findByEmail(adminEmail.toLowerCase()).isPresent()) {
                return;
            }

            User admin = User.builder()
                    .name(adminName)
                    .email(adminEmail.toLowerCase())
                    .password(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .createdAt(Instant.now())
                    .build();

            userRepository.save(admin);
        };
    }
}

