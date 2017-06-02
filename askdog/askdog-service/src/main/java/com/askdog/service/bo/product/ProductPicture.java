package com.askdog.service.bo.product;

import java.io.Serializable;

public class ProductPicture implements Serializable {

    private static final long serialVersionUID = -1506298199348483650L;

    private String name;
    private Long linkId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }
}
