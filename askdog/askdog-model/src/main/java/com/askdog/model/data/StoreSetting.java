package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "ad_store_setting ")
public class StoreSetting extends Base implements Serializable {

    private static final long serialVersionUID = 810286454573266933L;

    private Long storeId;
    private boolean requirePosId;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public boolean isRequirePosId() {
        return requirePosId;
    }

    public void setRequirePosId(boolean requirePosId) {
        this.requirePosId = requirePosId;
    }
}
