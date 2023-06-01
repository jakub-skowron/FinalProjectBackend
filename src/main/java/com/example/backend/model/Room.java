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

    @NotBlank
    @Size(min = 2, max = 20)
    @Column(unique = true)
    private String identifier;

    @NotNull
    private Level level;

    @NotNull
    private boolean availability;

    //add the number of places - sitting and standing
    //numberOfPlaces

    enum Level {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN
    }
}



