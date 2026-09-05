package com.zerotrust.accesscontrol.service;

import org.springframework.stereotype.Service;

import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}