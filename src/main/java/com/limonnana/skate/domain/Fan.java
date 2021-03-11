package com.limonnana.skate.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Fan.
 */
@Document(collection = "fan")
public class Fan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @DBRef
    @Field("user")
    private User user;


    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fan)) {
            return false;
        }
        return id != null && id.equals(((Fan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        String result = "";
        if (user == null) {
            result = "Fan{" + "id=" + getId() + " , user: NULL }";
        } else {
            result = "Fan{" +
                "id=" + getId() +
                ", fullName='" + user.getFirstName() + " " + user.getLastName() + "'" +
                ", phone='" + user.getPhone() + "'" +
                ", email='" + user.getEmail() + "'" +
                ", country='" + user.getCountry() + "'" +
                "}";
        }
        return result;
    }




    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
