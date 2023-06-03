package com.example.backend.controller;

import com.example.backend.model.Room;
import com.example.backend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    RoomService roomService;

    @GetMapping("")
    public List<Room> getAllRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable long id) {
        return roomService.getRoomById(id);
    }

    @PostMapping("")
    public void addOneRoom(@RequestBody Room room) {
        roomService.addRoom(room);
    }

    @DeleteMapping("/{id}")
    public void removeOneRoom(@PathVariable Long id) {
        roomService.removeRoomById(id);
    }

    @PutMapping("/{id}")
    public void updateOneRoom(@PathVariable Long id, @RequestBody Room room) {
        roomService.updateRoomNameById(id, room);
    }
}
