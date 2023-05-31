package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.repository.OrganizationRepository;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class OrganizationServiceTest {
    private Validator validator;
    @Mock
    OrganizationRepository organizationRepository;

    @Resource
    @InjectMocks
    OrganizationService organizationService;

    @BeforeEach
    void onInit() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void addOrganizationWithValidPayloadShouldPass() {
        long id = 1;
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName("Valid Name"); // Name length = 10
        organizationService.addOrganization(organization);
        verify(organizationRepository).save(organization);
    }

    @Test
    public void addOrganizationWithTooShortNameShouldThrowException() {
        long id = 1;
        String shortName = "A"; // Name is too short (min = 2)
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(shortName);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        assertEquals(1, violations.size());

        ConstraintViolation<Organization> violation = violations.iterator().next();
        assertEquals("size must be between 2 and 20", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    public void addOrganizationWithTooLongNameShouldThrowException() {
        long id = 1;
        String longName = "This name is too long"; // Name is too long (max = 20)
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(longName);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        assertEquals(1, violations.size());

        ConstraintViolation<Organization> violation = violations.iterator().next();
        assertEquals("size must be between 2 and 20", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    public void addOrganizationWithEmptyNameShouldThrowException() {
        long id = 1;
        String emptyName = ""; // Name is an empty String
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(emptyName);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        assertEquals(2, violations.size());
        assertTrue(violations.toString().contains("size must be between 2 and 20"));
        assertTrue(violations.toString().contains("must not be blank"));
    }

    @Test
    public void addOrganizationWithoutNameShouldThrowException() {
        long id = 1;
        // Without setting name
        Organization organization = new Organization();
        organization.setId(id);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        assertEquals(1, violations.size());
        assertTrue(violations.toString().contains("must not be blank"));
    }

    @Test
    public void addOrganizationWithExistingIdShouldThrowException() {
        long id = 1;
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName("Valid Name");
        when(organizationRepository.existsById(id)).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addOrganization(organization);
        });
    }

    @Test
    public void addOrganizationWithExistingNameShouldThrowException() {
        long id = 1;
        String existingName = "Existing Name";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(existingName);

        when(organizationRepository.existsByName(existingName)).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addOrganization(organization);
        });
    }
}