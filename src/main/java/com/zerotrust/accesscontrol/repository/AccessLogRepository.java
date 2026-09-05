package com.zerotrust.accesscontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerotrust.accesscontrol.entity.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}