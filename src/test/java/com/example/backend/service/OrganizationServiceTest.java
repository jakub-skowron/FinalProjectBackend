package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.repository.OrganizationRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class OrganizationServiceTest {
    @Mock
    OrganizationRepository organizationRepository;

    @InjectMocks
    OrganizationService organizationService;

    @BeforeEach
    void onInit() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addOrganizationWithValidPayloadShouldPass() {
        long id = 1;
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName("Valid Name"); // Name length = 10
        organizationService.addOrganization(organization);
        verify(organizationRepository, times(1)).save(organization);
    }

    @Test
    public void addOrganizationWithTooShortNameShouldThrowException() {
        long id = 1;
        String shortName = "A"; // Name is too short (min = 2)
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(shortName);

        try {
            organizationService.addOrganization(organization);
        } catch (ConstraintViolationException e) {
            assertTrue(e.getConstraintViolations().stream()
                    .anyMatch(violation -> violation.getMessage().equals("size must be between 2 and 20")));
        }
    }

    @Test
    public void addOrganizationWithTooLongNameShouldThrowException() {
        long id = 1;
        String longName = "This name is too long"; // Name is too long (max = 20)
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(longName);

        try {
            organizationService.addOrganization(organization);
        } catch (ConstraintViolationException e) {
            assertTrue(e.getConstraintViolations().stream()
                    .anyMatch(violation -> violation.getMessage().equals("size must be between 2 and 20")));
        }
    }

    @Test
    public void addOrganizationWithEmptyNameShouldThrowException() {
        long id = 1;
        String emptyName = ""; // Name is an empty String
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(emptyName);

        try {
            organizationService.addOrganization(organization);
        } catch (ConstraintViolationException e) {
            assertTrue(e.getConstraintViolations().stream()
                    .anyMatch(violation -> violation.getMessage().equals("must not be blank")));
            assertTrue(e.getConstraintViolations().stream()
                    .anyMatch(violation -> violation.getMessage().equals("size must be between 2 and 20")));
        }
    }

}