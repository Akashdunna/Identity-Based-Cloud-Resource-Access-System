package com.zerotrust.accesscontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerotrust.accesscontrol.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}