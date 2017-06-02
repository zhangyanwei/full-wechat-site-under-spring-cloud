package com.askdog.service.impl;

import com.askdog.model.data.inner.ResourceType;
import com.askdog.service.StorageService;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.storage.AccessToken;
import com.askdog.service.storage.Provider;
import com.askdog.service.storage.StorageAgent;
import com.askdog.service.storage.StorageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

import static com.askdog.model.data.inner.StorageProvider.ALI_OSS;

@RestController
public class StorageServiceImpl implements StorageService {

    @Provider(ALI_OSS)
    @Autowired private StorageAgent storageAgent;

    @Nonnull
    @Override
    public AccessToken generateAccessToken(@RequestParam("type") @Nonnull ResourceType type,
                                           @RequestParam("extention") @Nonnull String extention) throws ServiceException {
        return storageAgent.generateAccessToken(type, extention);
    }

    @Nonnull
    @Override
    public StorageResource persistResource(@RequestParam("resourceId") @Nonnull Long resourceId) throws ServiceException {
        return storageAgent.persistResource(resourceId);
    }
}
