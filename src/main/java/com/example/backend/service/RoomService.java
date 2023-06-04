package com.example.backend.service;

import com.example.backend.exceptions.ObjectAlreadyExistsException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.model.Room;
import com.example.backend.repository.OrganizationRepository;
import com.example.backend.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    public List<Room> getRooms() {
        LOGGER.info("List all rooms");
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        LOGGER.info("Searching for room with id {}", id);
        if (roomRepository.existsById(id)) {
            LOGGER.info("The room with id {} was found", id);
            return roomRepository.findById(id).get();
        } else {
            LOGGER.debug("The room with id {} not found", id);
            throw new ObjectNotFoundException("The room with inserted id doesn't exist");
        }
    }

    public void addRoom(Room room) {
        LOGGER.info("Room adding");
        long organizationId = room.getOrganizationId();
        if (roomRepository.existsById(room.getId())){
            LOGGER.debug("The room with inserted id already exists");
            throw new ObjectAlreadyExistsException("The Room id already exists!");
        }
        if (roomRepository.existsByName(room.getName())){
            LOGGER.debug("The room with inserted name already exists");
            throw new ObjectAlreadyExistsException("The Room name already exists!");
        }
        if (roomRepository.existsByIdentifier(room.getIdentifier())){
            LOGGER.debug("The room with inserted identifier already exists");
            throw new ObjectAlreadyExistsException("The Room identifier already exists!");
        }
        if (!organizationRepository.existsById(organizationId)) {
            LOGGER.debug("There is no room with inserted id");
            throw new ObjectNotFoundException("The Organization with inserted id doesn't exist!");
        }
        LOGGER.info("Room setting");
        room.setOrganization(organizationService.getOrganizationById(organizationId));
        roomRepository.save(room);
        LOGGER.info("The room was saved");
    }

    public void removeRoomById(Long id) {
        LOGGER.info("Room deleting");
        if (roomRepository.existsById(id)) {
            LOGGER.info("The room with id {} was found", id);
            roomRepository.deleteById(id);
            LOGGER.info("The room was deleted");
        } else {
            LOGGER.debug("The room with id {} not found", id);
            throw new ObjectNotFoundException("The Room with inserted id doesn't exist");
        }
    }

    //Entity To DTO Conversion for a Spring REST API
    public void updateRoomNameById(long id, Room room) {
        LOGGER.info("Reservation updating");
        long organizationId = room.getOrganizationId();
        if (roomRepository.existsById(id)) {
            LOGGER.info("The room with id {} was found", id);
            if (organizationRepository.existsById(organizationId)) {
                LOGGER.info("The organization with id {} was found", organizationId);
                LOGGER.info("Setting updated fields");
                room.setOrganization(organizationService.getOrganizationById(organizationId));
                room.setId(id);
                roomRepository.save(room);
                LOGGER.info("The room was updated");
            } else {
                LOGGER.debug("The organization with id {} not found", organizationId);
                throw new ObjectNotFoundException("The Organization with inserted id doesn't exist!");
            }
        } else {
            LOGGER.debug("The room with id {} not found", id);
            throw new ObjectNotFoundException("The Room with inserted id doesn't exist");
        }
    }
}
