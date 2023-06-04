package com.example.backend.service;

import com.example.backend.exceptions.ObjectNotFoundException;
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
    private Organization organization;
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

        long id = 1;
        String validName = "Valid Name";
        organization = new Organization();
        organization.setId(id);
        organization.setName(validName);
    }

    @Test
    public void ShouldNotReturnViolations() {
        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(0).isEqualTo(violations.size());
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"A, size must be between 2 and 20", "This name is too long, size must be between 2 and 20"})
    public void ifNameIsInvalidShouldReturnViolations(String name, String expectedErrorMessage) {
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
    public void ifNameIsEmptyStringShouldReturnViolations() {
        organization.setName("");

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(2).isEqualTo(violations.size());
        softly.assertThat(violations.toString()).contains("size must be between 2 and 20");
        softly.assertThat(violations.toString()).contains("must not be blank");
        softly.assertAll();
    }

    @Test
    public void ifIsNoNameShouldThrowException() {
        organization.setName(null);

        Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(violations.toString()).contains("must not be blank");
        softly.assertAll();
    }

    @Test
    public void addOrganizationWithExistingIdShouldThrowException() {
        when(organizationRepository.existsById(organization.getId())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.addOrganization(organization);
        });
    }

    @Test
    public void addOrganizationWithExistingNameShouldThrowException() {
        when(organizationRepository.existsByName(organization.getName())).thenReturn(true);

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

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            organizationService.getOrganizationById(id);
        });
    }

    @Test
    public void getOrganizationByIdShouldPass() {
        when(organizationRepository.existsById(organization.getId())).thenReturn(true);
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.of(organization));

        Organization result = organizationService.getOrganizationById(organization.getId());
        assertEquals(organization, result);
    }

    @Test
    public void deleteOrganizationByIdShouldPass() {
        when(organizationRepository.existsById(organization.getId())).thenReturn(true);

        organizationService.removeOrganizationById(organization.getId());
        verify(organizationRepository).deleteById(organization.getId());
    }

    @Test
    public void deleteOrganizationByIdWithInvalidIdShouldThrowException() {
        when(organizationRepository.existsById(organization.getId())).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            organizationService.removeOrganizationById(organization.getId());
        });
    }

    @Test
    public void updateOrganizationByIdShouldPass() {
        when(organizationRepository.existsById(organization.getId())).thenReturn(true);

        organizationService.updateOrganizationNameById(organization.getId(), organization);
        verify(organizationRepository).save(organization);
    }

    @Test
    public void updateOrganizationByIdWithInvalidIdShouldThrowException() {
        when(organizationRepository.existsById(organization.getId())).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            organizationService.updateOrganizationNameById(organization.getId(), organization);
        });
    }

    @Test
    public void addRoomToOrganizationShouldPass() {
        Room room = new Room.Builder()
                .withId(organization.getId())
                .withName("Random Name")
                .withAvailability(true)
                .withIdentifier("Random")
                .withLevel(1)
                .build();

        when(organizationRepository.existsById(organization.getId())).thenReturn(true);
        when(roomRepository.existsById(organization.getId())).thenReturn(true);
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.of(organization));
        when(roomRepository.findById(organization.getId())).thenReturn(Optional.of(room));

        organizationService.addRoomToOrganization(organization.getId(), room.getId());
        verify(roomRepository).save(room);
    }

    @Test
    public void addRoomToOrganizationWithInvalidRoomIdShouldThrowException() {
        Room room = new Room.Builder()
                .withId(organization.getId())
                .withName("Random Name")
                .withAvailability(true)
                .withIdentifier("Random")
                .withLevel(1)
                .build();

        when(organizationRepository.existsById(organization.getId())).thenReturn(true);
        when(roomRepository.existsById(organization.getId())).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            organizationService.addRoomToOrganization(organization.getId(), room.getId());
        });
    }

    @Test
    public void addRoomToOrganizationWithInvalidOrganizationIdShouldThrowException() {
        Room room = new Room.Builder()
                .withId(organization.getId())
                .withName("Random Name")
                .withAvailability(true)
                .withIdentifier("Random")
                .withLevel(1)
                .build();

        when(organizationRepository.existsById(organization.getId())).thenReturn(false);
        when(roomRepository.existsById(organization.getId())).thenReturn(true);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            organizationService.addRoomToOrganization(organization.getId(), room.getId());
        });
    }
}