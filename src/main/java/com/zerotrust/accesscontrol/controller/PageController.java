
package com.zerotrust.accesscontrol.controller;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.zerotrust.accesscontrol.entity.Device;
import com.zerotrust.accesscontrol.entity.Resource;
import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.repository.AccessLogRepository;
import com.zerotrust.accesscontrol.repository.AccessPolicyRepository;
import com.zerotrust.accesscontrol.repository.DeviceRepository;
import com.zerotrust.accesscontrol.repository.ResourceRepository;
import com.zerotrust.accesscontrol.repository.UserRepository;
import com.zerotrust.accesscontrol.service.AccessDecisionService;


@Controller
public class PageController {

    private final ResourceRepository resourceRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final AccessDecisionService accessDecisionService;
    private final AccessLogRepository accessLogRepository;
    private final AccessPolicyRepository accessPolicyRepository;

    public PageController(
        ResourceRepository resourceRepository,
        DeviceRepository deviceRepository,
        UserRepository userRepository,
        AccessDecisionService accessDecisionService,
        AccessLogRepository accessLogRepository,
        AccessPolicyRepository accessPolicyRepository) {

    this.resourceRepository = resourceRepository;
    this.deviceRepository = deviceRepository;
    this.userRepository = userRepository;
    this.accessDecisionService = accessDecisionService;
    this.accessLogRepository = accessLogRepository;
    this.accessPolicyRepository = accessPolicyRepository;
}

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
    return "access-denied";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        long resourceCount = resourceRepository.count();
        long deviceCount = deviceRepository.count();

        model.addAttribute("resourceCount", resourceCount);
        model.addAttribute("deviceCount", deviceCount);

        return "dashboard";
    }

    @GetMapping("/resources")
public String resources(Model model, Authentication authentication) {

    User user = userRepository.findByEmailIgnoreCase(authentication.getName())
            .orElse(null);

    if (user == null) {
        return "redirect:/login";
    }

    List<Resource> allResources = resourceRepository.findAll();

    List<Resource> allowedResources = allResources.stream()
            .filter(resource -> accessPolicyRepository.findAll()
                    .stream()
                    .anyMatch(policy ->
                            policy.getRole().getId().equals(user.getRole().getId())
                            && policy.getResource().getId().equals(resource.getId())
                    )
            )
            .toList();

    model.addAttribute("resources", allowedResources);

    return "resources";
}

@GetMapping("/sales-dashboard")
public String salesDashboard(Authentication authentication) {

    User user = userRepository.findByEmailIgnoreCase(authentication.getName())
            .orElse(null);

    Resource resource = resourceRepository.findById(1L)
            .orElse(null);

    if (user == null || resource == null) {
        return "redirect:/resources";
    }

    Device device = deviceRepository.findAll()
            .stream()
            .filter(d -> d.getUser().getId().equals(user.getId()))
            .findFirst()
            .orElse(null);

    String result = accessDecisionService.checkAccess(
            user,
            resource,
            device
    );

    if (result.startsWith("ALLOW")) {
        return "sales-dashboard";
    }

    return "redirect:/resources";
}

@GetMapping("/access-history")
public String accessHistory(Model model) {

    model.addAttribute("logs", accessLogRepository.findAll());

    return "access-history";
}


@GetMapping("/devices")
public String devices(Model model) {

    model.addAttribute("devices", deviceRepository.findAll());

    return "devices";
}




@GetMapping("/admin")
public String admin(Model model) {

    long totalUsers = userRepository.count();
    long totalDevices = deviceRepository.count();
    long totalResources = resourceRepository.count();
    long totalRequests = accessLogRepository.count();

    long allowedRequests = accessLogRepository.findAll()
            .stream()
            .filter(log -> "ALLOW".equals(log.getDecision()))
            .count();

    long deniedRequests = accessLogRepository.findAll()
            .stream()
            .filter(log -> "DENIED".equals(log.getDecision()))
            .count();

    model.addAttribute("totalUsers", totalUsers);
    model.addAttribute("users", userRepository.findAll());
    model.addAttribute("totalDevices", totalDevices);
    model.addAttribute("totalResources", totalResources);
    model.addAttribute("resources", resourceRepository.findAll());
    model.addAttribute("totalRequests", totalRequests);
    model.addAttribute("allowedRequests", allowedRequests);
    model.addAttribute("deniedRequests", deniedRequests);
    model.addAttribute("recentLogs",
        accessLogRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(5)
                .toList());

    return "admin";
}
}