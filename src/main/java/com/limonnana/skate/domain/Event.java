package com.limonnana.skate.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Event.
 */
@Document(collection = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("day")
    private LocalDate day;

    @Field("day_string")
    private String dayString;

    @NotNull
    @Field("name")
    private String name;

    @DBRef
    @Field("trick")
    private Set<Trick> tricks = new HashSet<>();

    @DBRef
    @Field("player")
    private Set<Player> players = new HashSet<>();


    @DBRef
    @Field("spot")
    private Spot spot;

    @DBRef
    @Field("photo")
    private Set<Photo> photos = new HashSet<>();

    @Field("active")
    private boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public Event day(LocalDate day) {
        this.day = day;
        return this;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getDayString() {
        return dayString;
    }

    public Event dayString(String dayString) {
        this.dayString = dayString;
        return this;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
    }

    public String getName() {
        return name;
    }

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Trick> getTricks() {
        return tricks;
    }

    public Event tricks(Set<Trick> tricks) {
        this.tricks = tricks;
        return this;
    }

    public Event addTrick(Trick trick) {
        this.tricks.add(trick);
        return this;
    }

    public Event removeTrick(Trick trick) {
        this.tricks.remove(trick);
        return this;
    }

    public void setTricks(Set<Trick> tricks) {
        this.tricks = tricks;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Event players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Event addPlayer(Player player) {
        this.players.add(player);
        return this;
    }

    public Event removePlayer(Player player) {
        this.players.remove(player);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }


    public Spot getSpot() {
        return spot;
    }

    public Event spot(Spot spot) {
        this.spot = spot;
        return this;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", day='" + getDay() + "'" +
            ", dayString='" + getDayString() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }


    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
