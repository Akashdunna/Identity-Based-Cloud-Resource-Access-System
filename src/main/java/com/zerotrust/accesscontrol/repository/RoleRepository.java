package com.zerotrust.accesscontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerotrust.accesscontrol.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}