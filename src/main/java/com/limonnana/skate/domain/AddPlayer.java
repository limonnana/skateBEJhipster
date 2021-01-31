package com.limonnana.skate.domain;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

public class AddPlayer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("idEvent")
    private String idEvent;
    @Field("idPlayer")
    private String idPlayer;

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }
}
