package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
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
    @JoinColumn(name = "organization_id", columnDefinition = "integer")
    private Organization organization;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(unique = true)
    private String name;

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(unique = true)
    private String identifier;

    @Min(0)
    @Max(10)
    @NotNull
    private int level;

    private boolean availability = true;

    @ElementCollection
    @CollectionTable(name = "room_places_mapping", joinColumns = @JoinColumn(name = "room_id"))
    @MapKeyColumn(name = "place_type")
    @Column(name = "number_of_places")
    @NotNull
    private Map<PlaceType, Integer> places;

    @Transient
    private long organizationId;

    public void setNumberOfPlaces(PlaceType placeType, int numberOfPlaces) {
        places.put(placeType, numberOfPlaces);
    }

    public int getNumberOfPlaces(PlaceType placeType) {
        return places.getOrDefault(placeType, 0);
    }

    public enum PlaceType {
        SITTING, STANDING
    }

    public static class Builder {
        private Room room;

        public Builder() {
            room = new Room();
        }

        public Builder withId(long id) {
            room.setId(id);
            return this;
        }

        public Builder withOrganization(Organization organization) {
            room.setOrganization(organization);
            return this;
        }

        public Builder withName(String name) {
            room.setName(name);
            return this;
        }

        public Builder withIdentifier(String identifier) {
            room.setIdentifier(identifier);
            return this;
        }

        public Builder withLevel(int level) {
            room.setLevel(level);
            return this;
        }

        public Builder withAvailability(boolean availability) {
            room.setAvailability(availability);
            return this;
        }

        public Builder withPlaces(Map<Room.PlaceType, Integer> places) {
            room.setPlaces(places);
            return this;
        }

        public Room build() {
            return room;
        }
    }
}



