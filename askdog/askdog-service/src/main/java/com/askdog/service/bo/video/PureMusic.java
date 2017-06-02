package com.askdog.service.bo.video;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;

public class PureMusic {

    @NotEmpty
    private String name;

    @DecimalMin("1")
    private float duration;

    @NotEmpty
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
