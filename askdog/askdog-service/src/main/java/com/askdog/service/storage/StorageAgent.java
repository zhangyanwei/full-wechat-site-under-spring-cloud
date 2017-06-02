package com.askdog.service.storage;

import com.askdog.model.data.StorageRecord;
import com.askdog.model.data.inner.ResourceType;
import com.askdog.service.exception.ServiceException;

import javax.annotation.Nonnull;
import java.io.InputStream;

public interface StorageAgent {

    @Nonnull
    AccessToken generateAccessToken(@Nonnull ResourceType type, @Nonnull String extention) throws ServiceException;

    @Nonnull
    StorageResource persistResource(@Nonnull Long linkId) throws ServiceException;

    @Nonnull
    StorageRecord save(@Nonnull ResourceType type, @Nonnull Long objectId, @Nonnull String fileName, @Nonnull InputStream stream) throws ServiceException;
}
