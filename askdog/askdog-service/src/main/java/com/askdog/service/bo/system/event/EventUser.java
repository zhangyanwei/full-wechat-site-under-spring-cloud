package com.askdog.service.bo.system.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class EventUser implements Serializable {

    private static final long serialVersionUID = -4103675785187009852L;

    @JsonFormat(shape = STRING)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public EventUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventUser setName(String name) {
        this.name = name;
        return this;
    }
}