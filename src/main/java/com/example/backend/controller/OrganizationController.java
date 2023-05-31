package com.example.backend.controller;

import com.example.backend.model.Organization;
import com.example.backend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
//@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @GetMapping("")
    public Set<Organization> getAllOrganizations() {
        return organizationService.getOrganizations();
    }

    @PostMapping("")
    public void addOneOrganization(@RequestBody Organization organization) {
        organizationService.addOrganization(organization);
    }

    @DeleteMapping("/{id}")
    public void removeOneOrganization(@PathVariable Long id) {
        organizationService.removeOrganization(id);
    }
}
