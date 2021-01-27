package com.limonnana.skate.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

public class AddTrick implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("idEvent")
    private String idEvent;
    @Field("idTrick")
    private String idTrick;

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getIdTrick() {
        return idTrick;
    }

    public void setIdTrick(String idTrick) {
        this.idTrick = idTrick;
    }
}
