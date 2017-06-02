package com.askdog.service.impl;

import com.askdog.model.data.inner.EntityType;
import com.askdog.model.data.inner.location.LocationDescription;
import com.askdog.service.LocationService;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.impl.location.LocationRecorder;
import com.askdog.service.location.LocationAgent;
import com.askdog.service.location.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

import static com.askdog.model.data.inner.location.LocationProvider.TENCENT_MAP;

@Service
@RestController
public class LocationServiceImpl implements LocationService {

    @Autowired
    @Provider(TENCENT_MAP)
    private LocationAgent locationAgent;

    @Autowired
    private LocationRecorder locationRecorder;

    @Override
    public boolean recordLocation(@Nonnull @RequestParam("type") EntityType type,
                                  @RequestParam("targetId") @Nonnull Long targetId,
                                  @RequestParam("ip") @Nonnull String ip,
                                  @RequestParam(name = "lat", required = false) Double lat,
                                  @RequestParam(name = "lng", required = false) Double lng) throws ServiceException {
        return locationAgent.recordLocation(type, targetId, ip, lat, lng);
    }

    @Nonnull
    @Override
    public LocationDescription[] suggestion(@RequestParam("ip") @Nonnull String ip, @RequestParam("lat") Double lat, @RequestParam("lng") Double lng) throws ServiceException {
        return locationAgent.suggestion(ip, lat, lng);
    }

    @Override
    public void updateUserResidence(@Nonnull @PathVariable("userId") Long userId) throws ServiceException {
        locationRecorder.updateUserResidence(userId);
    }

}
