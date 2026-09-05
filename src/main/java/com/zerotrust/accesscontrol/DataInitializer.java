package com.zerotrust.accesscontrol;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.zerotrust.accesscontrol.entity.AccessPolicy;
import com.zerotrust.accesscontrol.entity.Device;
import com.zerotrust.accesscontrol.entity.Resource;
import com.zerotrust.accesscontrol.entity.Role;
import com.zerotrust.accesscontrol.entity.User;
import com.zerotrust.accesscontrol.repository.AccessPolicyRepository;
import com.zerotrust.accesscontrol.repository.DeviceRepository;
import com.zerotrust.accesscontrol.repository.ResourceRepository;
import com.zerotrust.accesscontrol.repository.RoleRepository;
import com.zerotrust.accesscontrol.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            DeviceRepository deviceRepository,
            ResourceRepository resourceRepository,
            AccessPolicyRepository accessPolicyRepository) {

        return args -> {

            // Prevent duplicate data
            if (roleRepository.count() > 0) {
                return;
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // =========================
            // 1. ROLES
            // =========================

            Role adminRole = new Role("ADMIN");
            Role managerRole = new Role("MANAGER");
            Role employeeRole = new Role("EMPLOYEE");
            Role securityRole = new Role("SECURITY_ANALYST");

            roleRepository.save(adminRole);
            roleRepository.save(managerRole);
            roleRepository.save(employeeRole);
            roleRepository.save(securityRole);

            // =========================
            // 2. USERS
            // =========================

            User admin = new User(
                    "Admin User",
                    "admin@example.com",
                    encoder.encode("admin123"),
                    adminRole,
                    "ACTIVE"
            );

            User manager = new User(
                    "Sarah Manager",
                    "manager@example.com",
                    encoder.encode("manager123"),
                    managerRole,
                    "ACTIVE"
            );

            User employee = new User(
                    "John Employee",
                    "john@example.com",
                    encoder.encode("john123"),
                    employeeRole,
                    "ACTIVE"
            );

            User securityAnalyst = new User(
                    "Security Analyst",
                    "security@example.com",
                    encoder.encode("security123"),
                    securityRole,
                    "ACTIVE"
            );

            userRepository.save(admin);
            userRepository.save(manager);
            userRepository.save(employee);
            userRepository.save(securityAnalyst);

            // =========================
            // 3. DEVICES
            // =========================

            Device adminDevice = new Device(
                    admin,
                    "Admin-MacBook",
                    "LAPTOP",
                    "macOS",
                    true,
                    "SECURE",
                    LocalDateTime.now()
            );

            Device managerDevice = new Device(
                    manager,
                    "Sarah-Laptop",
                    "LAPTOP",
                    "Windows 11",
                    true,
                    "SECURE",
                    LocalDateTime.now()
            );

            Device employeeDevice = new Device(
                    employee,
                    "John-Laptop",
                    "LAPTOP",
                    "Windows 11",
                    true,
                    "SECURE",
                    LocalDateTime.now()
            );

            Device securityDevice = new Device(
                    securityAnalyst,
                    "Security-Workstation",
                    "DESKTOP",
                    "Ubuntu",
                    true,
                    "SECURE",
                    LocalDateTime.now()
            );

            deviceRepository.save(adminDevice);
            deviceRepository.save(managerDevice);
            deviceRepository.save(employeeDevice);
            deviceRepository.save(securityDevice);

            // =========================
            // 4. RESOURCES
            // =========================

            Resource salesDashboard = new Resource(
                    "Sales Analytics Dashboard",
                    "Company sales performance and analytics",
                    "DASHBOARD",
                    "MEDIUM",
                    "Cloud Analytics",
                    "ACTIVE"
            );

            Resource financialReports = new Resource(
                    "Financial Reports",
                    "Confidential company financial reports",
                    "DOCUMENT",
                    "HIGH",
                    "Cloud Storage",
                    "ACTIVE"
            );

            Resource employeeRecords = new Resource(
                    "Employee Records",
                    "Employee information and HR records",
                    "DATABASE",
                    "HIGH",
                    "HR Database",
                    "ACTIVE"
            );

            Resource projectDocuments = new Resource(
                    "Project Documents",
                    "Internal project documents and files",
                    "DOCUMENT",
                    "MEDIUM",
                    "Cloud Storage",
                    "ACTIVE"
            );

            Resource securityLogs = new Resource(
                    "Server & Security Logs",
                    "System and security monitoring logs",
                    "LOGS",
                    "CRITICAL",
                    "Security Cloud",
                    "ACTIVE"
            );

            Resource securityConfig = new Resource(
                    "Security Configuration",
                    "Security policies and configuration settings",
                    "CONFIGURATION",
                    "CRITICAL",
                    "Security Cloud",
                    "ACTIVE"
            );

            resourceRepository.save(salesDashboard);
            resourceRepository.save(financialReports);
            resourceRepository.save(employeeRecords);
            resourceRepository.save(projectDocuments);
            resourceRepository.save(securityLogs);
            resourceRepository.save(securityConfig);

            // =========================
            // 5. ACCESS POLICIES
            // =========================

            // ADMIN → All resources

            createPolicy(
                    accessPolicyRepository,
                    salesDashboard,
                    adminRole
            );

            createPolicy(
                    accessPolicyRepository,
                    financialReports,
                    adminRole
            );

            createPolicy(
                    accessPolicyRepository,
                    employeeRecords,
                    adminRole
            );

            createPolicy(
                    accessPolicyRepository,
                    projectDocuments,
                    adminRole
            );

            createPolicy(
                    accessPolicyRepository,
                    securityLogs,
                    adminRole
            );

            createPolicy(
                    accessPolicyRepository,
                    securityConfig,
                    adminRole
            );

            // MANAGER → Sales + Financial + Projects

            createPolicy(
                    accessPolicyRepository,
                    salesDashboard,
                    managerRole
            );

            createPolicy(
                    accessPolicyRepository,
                    financialReports,
                    managerRole
            );

            createPolicy(
                    accessPolicyRepository,
                    projectDocuments,
                    managerRole
            );

            // EMPLOYEE → Project Documents

                createPolicy(
                accessPolicyRepository,
                projectDocuments,
                employeeRole
                );

            // SECURITY ANALYST → Security resources + Logs

            createPolicy(
                    accessPolicyRepository,
                    securityLogs,
                    securityRole
            );

            createPolicy(
                    accessPolicyRepository,
                    securityConfig,
                    securityRole
            );

            System.out.println("======================================");
            System.out.println("Zero Trust sample data created!");
            System.out.println("Users      : 4");
            System.out.println("Devices    : 4");
            System.out.println("Resources  : 6");
            System.out.println("Policies   : 13");
            System.out.println("======================================");
        };
    }

    private void createPolicy(
            AccessPolicyRepository repository,
            Resource resource,
            Role role) {

        AccessPolicy policy = new AccessPolicy(
                resource,
                role,
                "READ",
                LocalDateTime.now()
        );

        repository.save(policy);
    }
}