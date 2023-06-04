package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.model.Room;
import com.example.backend.repository.OrganizationRepository;
import com.example.backend.repository.RoomRepository;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class OrganizationServiceTest {
    private Validator validator;
    @Mock
    OrganizationRepository organizationRepository;

    @Mock
    RoomRepository roomRepository;

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
    public void addOrganizationWithValidNameShouldPass() {
        long id = 1;
        String validName = "Valid Name";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(validName);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(0).isEqualTo(violations.size());
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"A, size must be between 2 and 20", "This name is too long, size must be between 2 and 20"})
    public void addOrganizationWithInvalidNameShouldThrowException(String name, String expectedErrorMessage) {
        long id = 1;
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);
        ConstraintViolation<Organization> violation = violations.iterator().next();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(expectedErrorMessage).isEqualTo(violation.getMessage());
        softly.assertThat("name").isEqualTo(violation.getPropertyPath().toString());
        softly.assertAll();
    }

    @Test
    public void addOrganizationWithEmptyNameShouldThrowException() {
        long id = 1;
        String emptyName = "";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(emptyName);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(2).isEqualTo(violations.size());
        softly.assertThat(violations.toString()).contains("size must be between 2 and 20");
        softly.assertThat(violations.toString()).contains("must not be blank");
        softly.assertAll();
    }

    @Test
    public void addOrganizationWithoutNameShouldThrowException() {
        long id = 1;
        Organization organization = new Organization();
        organization.setId(id);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(violations.toString()).contains("must not be blank");
        softly.assertAll();
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

    @Test
    public void getOrganizationsShouldPass() {
        when(organizationRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(organizationService.getOrganizations().isEmpty());
    }

    @Test
    public void getOrganizationByIdWithInvalidIdShouldThrowException() {
        long id = 1;
        when(organizationRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.getOrganizationById(id);
        });
    }

    @Test
    public void getOrganizationByIdShouldPass() {
        long id = 1;
        String name = "Valid Name";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);
        when(organizationRepository.existsById(id)).thenReturn(true);
        when(organizationRepository.findById(id)).thenReturn(Optional.of(organization));

        Organization result = organizationService.getOrganizationById(id);
        assertEquals(organization, result);
    }

    @Test
    public void deleteOrganizationByIdShouldPass() {
        long id = 1;
        String name = "Valid Name";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);
        when(organizationRepository.existsById(id)).thenReturn(true);

        organizationService.removeOrganizationById(id);
        verify(organizationRepository).deleteById(id);
    }

    @Test
    public void deleteOrganizationByIdWithInvalidIdShouldThrowException() {
        long id = 1;
        when(organizationRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.removeOrganizationById(id);
        });
    }

    @Test
    public void updateOrganizationByIdShouldPass() {
        long id = 1;
        String name = "Valid Name";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);
        when(organizationRepository.existsById(id)).thenReturn(true);

        organizationService.updateOrganizationNameById(id, organization);
        verify(organizationRepository).save(organization);
    }

    @Test
    public void updateOrganizationByIdWithInvalidIdShouldThrowException() {
        long id = 1;
        String name = "Valid Name";
        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);
        when(organizationRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.updateOrganizationNameById(id, organization);
        });
    }

    @Test
    public void addRoomToOrganizationShouldPass() {
        long id = 1;
        String name = "Valid Name";

        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);

        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setAvailability(true);
        room.setIdentifier("Random Name");
        room.setLevel(1);

        when(organizationRepository.existsById(id)).thenReturn(true);
        when(roomRepository.existsById(id)).thenReturn(true);
        when(organizationRepository.findById(id)).thenReturn(Optional.of(organization));
        when(roomRepository.findById(id)).thenReturn(Optional.of(room));

        organizationService.addRoomToOrganization(organization.getId(), room.getId());
        verify(roomRepository).save(room);
    }

    @Test
    public void addRoomToOrganizationWithInvalidRoomIdShouldThrowException() {
        long id = 1;
        String name = "Valid Name";

        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);

        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setAvailability(true);
        room.setIdentifier("Random Name");
        room.setLevel(1);

        when(organizationRepository.existsById(id)).thenReturn(true);
        when(roomRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addRoomToOrganization(organization.getId(), room.getId());
        });
    }

    @Test
    public void addRoomToOrganizationWithInvalidOrganizationIdShouldThrowException() {
        long id = 1;
        String name = "Valid Name";

        Organization organization = new Organization();
        organization.setId(id);
        organization.setName(name);

        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setAvailability(true);
        room.setIdentifier("Random Name");
        room.setLevel(1);

        when(organizationRepository.existsById(id)).thenReturn(false);
        when(roomRepository.existsById(id)).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addRoomToOrganization(organization.getId(), room.getId());
        });
    }
}