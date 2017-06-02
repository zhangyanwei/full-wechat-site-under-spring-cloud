package com.askdog.model.data;

import com.askdog.model.data.inner.ResourceDescription;
import com.askdog.model.data.inner.ResourceState;
import com.askdog.model.data.inner.ResourceType;
import com.askdog.model.data.inner.StorageProvider;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "ad_storage_record")
public class StorageRecord extends Base implements Serializable {

    private static final long serialVersionUID = 5030436565718830177L;

    private Long resourceId;
    private StorageProvider provider;
    private Date creationDate;
    private ResourceType resourceType;
    private ResourceState resourceState;
    private ResourceDescription description;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public StorageProvider getProvider() {
        return provider;
    }

    public void setProvider(StorageProvider provider) {
        this.provider = provider;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public ResourceState getResourceState() {
        return resourceState;
    }

    public void setResourceState(ResourceState resourceState) {
        this.resourceState = resourceState;
    }

    public ResourceDescription getDescription() {
        return description;
    }

    public void setDescription(ResourceDescription description) {
        this.description = description;
    }

}

