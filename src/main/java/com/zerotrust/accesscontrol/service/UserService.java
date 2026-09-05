package com.zerotrust.accesscontrol.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}