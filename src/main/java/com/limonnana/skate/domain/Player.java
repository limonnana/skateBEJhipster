package com.limonnana.skate.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * A Player.
 */
@Document(collection = "player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @DBRef
    @Field("user")
    private User user;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return getId() != null && getId().equals(((Player) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        User u = getUser();
        return "Player{" +
            "id=" + getId() +
            ", fullName='" + getUser().getFirstName() + " " + getUser().getLastName() + "'" +
            ", phone='" + getUser().getPhone() + "'" +
            ", email='" + getUser().getEmail() + "'" +
            ", country='" + getUser().getCountry() + "'" +
            "}";
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
