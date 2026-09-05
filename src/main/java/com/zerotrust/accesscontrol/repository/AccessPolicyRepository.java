package com.zerotrust.accesscontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerotrust.accesscontrol.entity.AccessPolicy;

public interface AccessPolicyRepository extends JpaRepository<AccessPolicy, Long> {
}