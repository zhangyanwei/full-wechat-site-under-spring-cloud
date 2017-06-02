package com.askdog.service.bo.event;

import javax.validation.constraints.NotNull;

public class AmendedEvent extends EventData {

    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
