package com.example.backend.repository;

import com.example.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByName(String string);

    boolean existsByIdentifier(String string);

    boolean existsByNameAndIdNot(String string, long id);
    boolean existsByIdentifierAndIdNot(String string, long id);
}
