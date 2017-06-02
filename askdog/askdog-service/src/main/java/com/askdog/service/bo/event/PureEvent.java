package com.askdog.service.bo.event;

import javax.validation.constraints.NotNull;

public class PureEvent extends EventData {

    @NotNull
    private Long store;

    public Long getStore() {
        return store;
    }

    public void setStore(Long store) {
        this.store = store;
    }
}
