package com.zerotrust.accesscontrol.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerotrust.accesscontrol.entity.Resource;
import com.zerotrust.accesscontrol.service.ResourceService;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    @GetMapping("/{id}")
    public Resource getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id);
    }

    @PostMapping
    public Resource createResource(@RequestBody Resource resource) {
        return resourceService.saveResource(resource);
    }

    @PutMapping("/{id}/status")
    public Resource updateResourceStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        Resource resource = resourceService.getResourceById(id);

        if (resource == null) {
            return null;
        }

        resource.setStatus(status.toUpperCase());

        return resourceService.saveResource(resource);
    }
}