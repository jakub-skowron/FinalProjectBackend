package com.example.backend.service;

import com.example.backend.exceptions.ObjectAlreadyExistsException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.model.Room;
import com.example.backend.repository.OrganizationRepository;
import com.example.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    OrganizationRepository organizationRepository;

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        if (roomRepository.existsById(id)) {
            return roomRepository.findById(id).get();
        } else {
            throw new ObjectNotFoundException("The Room with inserted id doesn't exist");
        }
    }

    public void addRoom(Room room) {
        long organizationId = room.getOrganizationId();
        boolean roomExists = roomRepository.existsById(room.getId())
                || roomRepository.existsByName(room.getName())
                || roomRepository.existsByIdentifier(room.getIdentifier());

        if (!roomExists) {
            if (organizationRepository.existsById(organizationId)) {
                room.setOrganization(organizationService.getOrganizationById(organizationId));
                roomRepository.save(room);
            } else {
                throw new ObjectNotFoundException("The Organization with inserted id doesn't exist!");
            }
        } else {
            throw new ObjectAlreadyExistsException("The Room name, id or identifier already exists!");
        }
    }

    public void removeRoomById(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("The Room with inserted id doesn't exist");
        }
    }

    //Entity To DTO Conversion for a Spring REST API
    public void updateRoomNameById(long id, Room room) {
        long organizationId = room.getOrganizationId();
        if (roomRepository.existsById(id)) {
            if (organizationRepository.existsById(organizationId)) {
                room.setOrganization(organizationService.getOrganizationById(organizationId));
                room.setId(id);
                roomRepository.save(room);
            } else {
                throw new ObjectNotFoundException("The Organization with inserted id doesn't exist!");
            }
        } else {
            throw new ObjectNotFoundException("The Room with inserted id doesn't exist");
        }
    }
}
