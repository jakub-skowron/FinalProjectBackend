package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public Set<Organization> getOrganizations() {
        return new HashSet<>(organizationRepository.findAll());
    }

    public void addOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    public void removeOrganization(Long id) {
        organizationRepository.deleteById(id);
    }
}
