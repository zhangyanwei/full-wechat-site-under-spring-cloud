package com.askdog.model.data.video;

import java.io.Serializable;

public class VideoSnapshot implements Serializable {

    private static final long serialVersionUID = 4824185401680420803L;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
