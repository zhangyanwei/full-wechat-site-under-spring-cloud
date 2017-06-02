package com.askdog.service.location;

import com.askdog.model.data.inner.EntityType;
import com.askdog.model.data.inner.location.LocationDescription;
import com.askdog.service.exception.ServiceException;

import javax.annotation.Nonnull;

public interface LocationAgent {

    boolean recordLocation(@Nonnull EntityType type, @Nonnull Long targetId, @Nonnull String ip, Double lat, Double lng) throws ServiceException;

    @Nonnull
    LocationDescription[] suggestion(@Nonnull String ip, Double lat, Double lng) throws ServiceException;

    ResponseBody analysisAddress(String ip);

    <T extends ResponseBody> T analysisAddress(Double lat, Double lng);

}

