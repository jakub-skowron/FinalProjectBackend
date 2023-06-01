package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "serial")
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "room_id", columnDefinition = "integer")
    private Organization organization;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(unique = true)
    private String name;

    @Size(min = 2, max = 20)
    @Column(unique = true)
    private String identifier;

    @NotNull
    private Level level;

    @NotNull
    private boolean availability;

    @ElementCollection
    @CollectionTable(name = "room_places_mapping", joinColumns = @JoinColumn(name = "room_id"))
    @MapKeyColumn(name = "place_type")
    @Column(name = "number_of_places")
    private Map<PlaceType, Integer> places;

    public void setNumberOfPlaces(PlaceType placeType, int numberOfPlaces) {
        places.put(placeType, numberOfPlaces);
    }

    public int getNumberOfPlaces(PlaceType placeType) {
        return places.getOrDefault(placeType, 0);
    }

    public enum Level {
        ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5),
        SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);

        private final int number;

        Level(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }
    public enum PlaceType {
        SITTING, STANDING
    }
}



