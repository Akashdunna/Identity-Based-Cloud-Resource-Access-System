package com.zerotrust.accesscontrol.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerotrust.accesscontrol.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}