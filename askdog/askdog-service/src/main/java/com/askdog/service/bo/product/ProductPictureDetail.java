package com.askdog.service.bo.product;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

public class ProductPictureDetail implements Serializable {

    private static final long serialVersionUID = 1428590611379302801L;

    @JsonFormat(shape = STRING)
    private Long linkId;
    private String name;
    private String url;

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
