package com.limonnana.skate.domain;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class AddImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Field("idEvent")
    private String idEvent;

    @Field("title")
    private String title;

    @Field("file")
    private MultipartFile file;

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
