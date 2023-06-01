package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.model.Room;
import com.example.backend.repository.OrganizationRepository;
import com.example.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RoomRepository roomRepository;

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

    public void updateOrganizationNameById(long id, Organization organization) {
        if (organizationRepository.existsById(id)) {
            organization.setId(id);
            organizationRepository.save(organization);
        } else {
            throw new IllegalArgumentException("The Organization with inserted id doesn't exist");
        }
    }

    public void addRoomToOrganization(long organizationId, long roomId) {
        if (organizationRepository.existsById(organizationId) && roomRepository.existsById(roomId)) {
            Organization organization = organizationRepository.findById(organizationId).get();
            Room room = roomRepository.findById(roomId).get();
            if (room.isAvailability()){
                room.setOrganization(organization);
                roomRepository.save(room);
            }
            else{
                throw new IllegalArgumentException("The room is already booked");
            }
        } else {
            throw new IllegalArgumentException("There is no organization or room with inserted id");
        }
    }
}
