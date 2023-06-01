package com.example.backend.service;

import com.example.backend.model.Organization;
import com.example.backend.model.Room;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class RoomServiceTest {
    private Validator validator;
    private Room room;
    private Map<Room.PlaceType, Integer> places;
    @Mock
    RoomRepository roomRepository;
    @Resource
    @InjectMocks
    RoomService roomService;

    @BeforeEach
    void onInit() {
        MockitoAnnotations.initMocks(this);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        places = new HashMap<>();
        places.put(Room.PlaceType.SITTING, 3);
        places.put(Room.PlaceType.STANDING, 4);
        room = new Room.Builder()
                .withId(1)
                .withName("Valid Name")
                .withIdentifier("Valid Identifier")
                .withLevel(1)
                .withPlaces(places)
                .build();
    }

    @Test
    public void addRoomWithValidPayloadShouldPass() {

        Set<ConstraintViolation<Room>> violations = validator.validate(room);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(0).isEqualTo(violations.size());
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"A, size must be between 2 and 20", "This name is too long, size must be between 2 and 20"})
    public void addRoomWithInvalidNameShouldThrowException(String name, String expectedErrorMessage) {
        room.setName(name);

        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        ConstraintViolation<Room> violation = violations.iterator().next();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(expectedErrorMessage).isEqualTo(violation.getMessage());
        softly.assertThat("name").isEqualTo(violation.getPropertyPath().toString());
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"A, size must be between 2 and 20", "This name is too long, size must be between 2 and 20"})
    public void addRoomWithInvalidIdentifierShouldThrowException(String identifier, String expectedErrorMessage) {
        room.setIdentifier(identifier);

        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        ConstraintViolation<Room> violation = violations.iterator().next();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(expectedErrorMessage).isEqualTo(violation.getMessage());
        softly.assertThat("identifier").isEqualTo(violation.getPropertyPath().toString());
        softly.assertAll();
    }

    @Test
    public void addRoomWithExistedNameShouldThrowException() {

        when(roomRepository.existsByName(room.getName())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            roomService.addRoom(room);
        });
    }

    @Test
    public void addRoomWithExistedIdShouldThrowException() {

        when(roomRepository.existsById(room.getId())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            roomService.addRoom(room);
        });
    }

    @Test
    public void addRoomWithExistedIdentifierShouldThrowException() {

        when(roomRepository.existsByIdentifier(room.getIdentifier())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            roomService.addRoom(room);
        });
    }

    @ParameterizedTest
    @CsvSource({"-1, must be greater than or equal to 0", "11, must be less than or equal to 10"})
    public void addRoomWithInvalidLevelShouldThrowException(Integer number, String expectedErrorMessage) {
        room.setLevel(number);

        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        ConstraintViolation<Room> violation = violations.iterator().next();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(expectedErrorMessage).isEqualTo(violation.getMessage());
        softly.assertThat("level").isEqualTo(violation.getPropertyPath().toString());
        softly.assertAll();
    }

    @Test
    public void addRoomWithoutPlacesShouldThrowException() {
        room.setPlaces(null);

        Set<ConstraintViolation<Room>> violations = validator.validate(room);
        ConstraintViolation<Room> violation = violations.iterator().next();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat("must not be null").isEqualTo(violation.getMessage());
        softly.assertThat("places").isEqualTo(violation.getPropertyPath().toString());
        softly.assertAll();
    }

    @Test
    public void getRoomsShouldPass() {
        when(roomRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(roomService.getRooms().isEmpty());
    }

    @Test
    public void getRoomByIdShouldPass() {
        when(roomRepository.existsById(room.getId())).thenReturn(true);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(room.getId());
        assertEquals(room, result);
    }
}