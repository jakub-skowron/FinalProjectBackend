package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public Set<Organization> getOrganizations() {
        return new HashSet<>(organizationRepository.findAll());
    }

    public Optional<Organization> getOrganizationById(Long id) {
        if (organizationRepository.existsById(id)) {
            return organizationRepository.findById(id);
        } else {
            throw new IllegalArgumentException("The Organization with inserted id doesn't exist");
        }
    }

    public void addOrganization(Organization organization) {
        if (!organizationRepository.existsById(organization.getId()) && !organizationRepository.existsByName(organization.getName())) {
            organizationRepository.save(organization);
        } else throw new IllegalArgumentException("The Organization name or id already exists!");
    }

    public void removeOrganizationById(Long id) {
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("The Organization with inserted id doesn't exist");
        }
    }
}
