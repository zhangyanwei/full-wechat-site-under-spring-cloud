package com.askdog.service.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class StorageResource {

    @JsonFormat(shape = STRING)
    private Long linkId;
    private String url;

    @JsonCreator
    public StorageResource(@JsonProperty("linkId") Long linkId, @JsonProperty("url") String url) {
        this.linkId = linkId;
        this.url = url;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
