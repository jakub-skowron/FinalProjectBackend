package com.example.backend.service;

import com.example.backend.exceptions.ObjectAlreadyExistsException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.model.Organization;
import com.example.backend.model.Room;
import com.example.backend.repository.OrganizationRepository;
import com.example.backend.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    Logger LOGGER = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RoomRepository roomRepository;

    public List<Organization> getOrganizations() {
        LOGGER.info("List all organizations");
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(Long id) {
        LOGGER.info("Searching for organization with id {}", id);
        if (organizationRepository.existsById(id)) {
            LOGGER.info("The organization with id {} was found", id);
            return organizationRepository.findById(id).get();
        } else {
            LOGGER.debug("The organization with id {} not found", id);
            throw new ObjectNotFoundException("There is no organization with inserted id");
        }
    }

    public void addOrganization(Organization organization) {
        LOGGER.info("Adding organization");
        if (organizationRepository.existsById(organization.getId())) {
            LOGGER.debug("The organization with id {} already exists", organization.getId());
            throw new ObjectAlreadyExistsException("The organization with inserted id already exists");
        }
        if (organizationRepository.existsByName(organization.getName())) {
            LOGGER.debug("The organization with name {} already exists", organization.getName());
            throw new ObjectAlreadyExistsException("The organization with inserted name already exists");
        }
        organizationRepository.save(organization);
        LOGGER.info("The organization was saved");
    }

    public void removeOrganizationById(Long id) {
        LOGGER.info("Deleting organization");
        if (organizationRepository.existsById(id)) {
            LOGGER.info("The organization with id {} was found", id);
            organizationRepository.deleteById(id);
            LOGGER.info("The organization was deleted");
        } else {
            LOGGER.debug("The organization with id {} not found", id);
            throw new ObjectNotFoundException("There is no organization with inserted id");
        }
    }

    public void updateOrganizationNameById(long id, Organization organization) {
        LOGGER.info("Updating organization");
        if (organizationRepository.existsById(id)) {
            LOGGER.info("The organization with id {} was found", id);
            LOGGER.info("Setting updated fields");
            organization.setId(id);
            organizationRepository.save(organization);
            LOGGER.info("The organization was updated");
        } else {
            LOGGER.debug("The organization with id {} not found", id);
            throw new ObjectNotFoundException("There is no organization with inserted id");
        }
    }

    public void addRoomToOrganization(long organizationId, long roomId) {
        LOGGER.info("Adding room to organization");
        if (!organizationRepository.existsById(organizationId)) {
            LOGGER.debug("The organization with id {} not found", organizationId);
            throw new ObjectNotFoundException("There is no organization with inserted id");
        }
        LOGGER.info("The organization with id {} was found", organizationId);
        if (roomRepository.existsById(roomId)) {
            LOGGER.info("The room with id {} was found", roomId);
            Organization organization = organizationRepository.findById(organizationId).get();
            Room room = roomRepository.findById(roomId).get();
            LOGGER.info("Setting room to organization");
            room.setOrganization(organization);
            roomRepository.save(room);
            LOGGER.info("The room was added to organization");
        } else {
            LOGGER.debug("The room with id {} not found", roomId);
            throw new ObjectNotFoundException("There is no room with inserted id");
        }
    }
}
