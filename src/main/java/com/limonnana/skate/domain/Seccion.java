package com.limonnana.skate.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

public class Seccion {

    @Id
    private String id;

    @DBRef
    @Field("user")
    private User user;

    @Field("porcentaje")
    private int porcentaje;


    @Field("shekel")
    private int shekel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getShekel() {
        return shekel;
    }

    public void setShekel(int shekel) {
        this.shekel = shekel;
    }


}
