package com.zerotrust.accesscontrol.service;

import org.springframework.stereotype.Service;

import com.zerotrust.accesscontrol.entity.AccessLog;
import com.zerotrust.accesscontrol.entity.AccessPolicy;
import com.zerotrust.accesscontrol.entity.Device;
import com.zerotrust.accesscontrol.entity.Resource;
import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.repository.AccessLogRepository;
import com.zerotrust.accesscontrol.repository.AccessPolicyRepository;

@Service
public class AccessDecisionService {

    private final AccessPolicyRepository accessPolicyRepository;
    private final AccessLogRepository accessLogRepository;

    public AccessDecisionService(
        AccessPolicyRepository accessPolicyRepository,
        AccessLogRepository accessLogRepository) {

    this.accessPolicyRepository = accessPolicyRepository;
    this.accessLogRepository = accessLogRepository;
}

public String checkAccess(User user, Resource resource, Device device) {

    String decision;
    String reason;

    if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {

        decision = "DENIED";
        reason = "User account is not active";

    } else if (device == null || !device.isRegistered()) {

        decision = "DENIED";
        reason = "Device is not registered";

    } else if (!"SECURE".equalsIgnoreCase(device.getSecurityStatus())) {

        decision = "DENIED";
        reason = "Device is not secure";
    
    } else if (!"ACTIVE".equalsIgnoreCase(resource.getStatus())) {
    
        decision = "DENIED";
        reason = "Resource is not active";
    
    } else {

        AccessPolicy policy = accessPolicyRepository.findAll()
                .stream()
                .filter(p -> p.getRole().getId().equals(user.getRole().getId()))
                .filter(p -> p.getResource().getId().equals(resource.getId()))
                .findFirst()
                .orElse(null);

                if (policy == null) {

                    decision = "DENIED";
                    reason = "No access policy exists";
                
                } else if (!"READ".equalsIgnoreCase(policy.getPermission())) {
                
                    decision = "DENIED";
                    reason = "Required permission is not available";
                
                } else {
                
                    decision = "ALLOW";
                    reason = "Access granted";
                }
    }

    AccessLog accessLog = new AccessLog(
            user,
            resource,
            device,
            decision,
            reason,
            "127.0.0.1",
            java.time.LocalDateTime.now()
    );

    accessLogRepository.save(accessLog);

    return decision + ": " + reason;
}
}