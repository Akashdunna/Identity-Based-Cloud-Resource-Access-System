package com.zerotrust.accesscontrol.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerotrust.accesscontrol.entity.Device;
import com.zerotrust.accesscontrol.repository.DeviceRepository;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}/security")
public Device updateSecurityStatus(
        @PathVariable Long id,
        @RequestParam String status) {

    Device device = deviceRepository.findById(id)
            .orElse(null);

    if (device == null) {
        return null;
    }

    device.setSecurityStatus(status.toUpperCase());

    return deviceRepository.save(device);
}

@PutMapping("/{id}/registration")
public Device updateRegistrationStatus(
        @PathVariable Long id,
        @RequestParam boolean registered) {

    Device device = deviceRepository.findById(id)
            .orElse(null);

    if (device == null) {
        return null;
    }

    device.setRegistered(registered);

    return deviceRepository.save(device);
}
}