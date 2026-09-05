package com.zerotrust.accesscontrol.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/user")
    public User getUser(@RequestParam String email) {
        return authService.findUserByEmail(email);
    }
}