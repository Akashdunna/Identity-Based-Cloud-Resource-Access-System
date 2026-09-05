package com.zerotrust.accesscontrol.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zerotrust.accesscontrol.entity.Resource;
import com.zerotrust.accesscontrol.repository.ResourceRepository;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id).orElse(null);
    }

    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }
}