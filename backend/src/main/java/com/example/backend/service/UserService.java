package com.example.backend.service;

import com.example.backend.dto.UserRequest;
import com.example.backend.dto.UserResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
            ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new RuntimeException("Email is already registered");
    }

    User user = new User();

    user.setName(request.getName());
    user.setEmail(request.getEmail());
    
    user.setPassword(
            passwordEncoder.encode(request.getPassword())
    );

    user.setRole("USER");

    User savedUser = userRepository.save(user);

    return new UserResponse(
            savedUser.getId(),
            savedUser.getName(),
            savedUser.getEmail(),
            savedUser.getRole()
    );
}
}