package com.zerotrust.accesscontrol.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerotrust.accesscontrol.entity.Device;
import com.zerotrust.accesscontrol.entity.Resource;
import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.repository.DeviceRepository;
import com.zerotrust.accesscontrol.repository.UserRepository;
import com.zerotrust.accesscontrol.service.AccessDecisionService;
import com.zerotrust.accesscontrol.service.ResourceService;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessDecisionService accessDecisionService;
    private final UserRepository userRepository;
    private final ResourceService resourceService;
    private final DeviceRepository deviceRepository;

    public AccessController(
            AccessDecisionService accessDecisionService,
            UserRepository userRepository,
            ResourceService resourceService,
            DeviceRepository deviceRepository) {

        this.accessDecisionService = accessDecisionService;
        this.userRepository = userRepository;
        this.resourceService = resourceService;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/check")
    public String checkAccess(
            @RequestParam Long resourceId,
            Authentication authentication) {

        // Get the currently logged-in user's email
        String email = authentication.getName();

        // Find the user from database
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        if (user == null) {
            return "DENIED: User not found";
        }

        // Find requested resource
        Resource resource = resourceService.getResourceById(resourceId);

        if (resource == null) {
            return "DENIED: Resource not found";
        }

        // Find a device belonging to the logged-in user
        Device device = deviceRepository.findAll()
                .stream()
                .filter(d -> d.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        return accessDecisionService.checkAccess(
                user,
                resource,
                device
        );
    }
}