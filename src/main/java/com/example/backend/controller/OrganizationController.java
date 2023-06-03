package com.example.backend.controller;

import com.example.backend.model.Organization;
import com.example.backend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/organizations")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @GetMapping("")
    public List<Organization> getAllOrganizations() {
        return organizationService.getOrganizations();
    }

    @GetMapping("/{id}")
    public Organization getOrganizationById(@PathVariable long id) {
        return organizationService.getOrganizationById(id);
    }

    @PostMapping("")
    public void addOneOrganization(@RequestBody Organization organization) {
        organizationService.addOrganization(organization);
    }

    @DeleteMapping("/{id}")
    public void removeOneOrganization(@PathVariable Long id) {
        organizationService.removeOrganizationById(id);
    }

    @PutMapping("/{id}")
    public void updateOneOrganization(@PathVariable Long id, @RequestBody Organization organization) {
        organizationService.updateOrganizationNameById(id, organization);
    }

    @PatchMapping("/{organizationId}/rooms/{roomId}")
    public void addOneRoomToOrganization(@PathVariable long organizationId,
                                         @PathVariable long roomId) {
        organizationService.addRoomToOrganization(organizationId, roomId);
    }
}
