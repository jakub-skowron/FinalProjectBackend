package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
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
}