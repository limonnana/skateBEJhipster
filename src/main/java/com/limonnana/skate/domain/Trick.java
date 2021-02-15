package com.limonnana.skate.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Trick.
 */
@Document(collection = "trick")
public class Trick implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("objective_amount")
    private Integer objectiveAmount = 0;

    @Field("current_amount")
    private Integer currentAmount = 0;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Trick name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getObjectiveAmount() {
        return objectiveAmount;
    }

    public Trick objectiveAmount(Integer objectiveAmount) {
        this.objectiveAmount = objectiveAmount;
        return this;
    }

    public void setObjectiveAmount(Integer objectiveAmount) {
        this.objectiveAmount = objectiveAmount;
    }

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public Trick currentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
        return this;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trick)) {
            return false;
        }
        return id != null && id.equals(((Trick) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trick{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", objectiveAmount=" + getObjectiveAmount() +
            ", currentAmount=" + getCurrentAmount() +
            "}";
    }
}
