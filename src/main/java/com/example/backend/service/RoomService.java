package com.example.backend.service;

import com.example.backend.model.Room;
import com.example.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    public Set<Room> getRooms() {
        return new HashSet<>(roomRepository.findAll());
    }

    public Room getRoomById(Long id) {
        if (roomRepository.existsById(id)) {
            return roomRepository.findById(id).get();
        } else {
            throw new IllegalArgumentException("The Room with inserted id doesn't exist");
        }
    }

    public void addRoom(Room room) {
        boolean roomExists = roomRepository.existsById(room.getId())
                || roomRepository.existsByName(room.getName())
                || roomRepository.existsByIdentifier(room.getIdentifier());

        if (!roomExists) {
            roomRepository.save(room);
        } else {
            throw new IllegalArgumentException("The Room name, id or identifier already exists!");
        }
    }

    public void removeRoomById(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("The Room with inserted id doesn't exist");
        }
    }

    public void updateRoomNameById(long id, Room room) {
        if (roomRepository.existsById(id)) {
            room.setId(id);
            roomRepository.save(room);
        } else {
            throw new IllegalArgumentException("The Room with inserted id doesn't exist");
        }
    }
}
