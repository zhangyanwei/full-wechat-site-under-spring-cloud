package com.askdog.service.impl.location.tencent;

import com.askdog.common.utils.Json;
import com.askdog.model.data.inner.EntityType;
import com.askdog.model.data.inner.location.LocationDescription;
import com.askdog.model.data.inner.location.LocationDescription.Location;
import com.askdog.model.data.inner.location.LocationProvider;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.impl.location.LocationConfiguration;
import com.askdog.service.impl.location.LocationRecorder;
import com.askdog.service.location.LocationAgent;
import com.askdog.service.location.Provider;
import com.askdog.service.location.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;

import static com.askdog.model.data.inner.location.LocationProvider.TENCENT_MAP;


@org.springframework.context.annotation.Configuration
@Component
@Provider(TENCENT_MAP)
@ConditionalOnBean(LocationConfiguration.class)
public class TencentLocationAgent implements LocationAgent {

    private final static int EXCLUDE_POI = 0;
    private final static int INCLUDE_POI = 1;

    @Autowired
    private LocationRecorder locationRecorder;

    @Autowired
    private LocationConfiguration locationConfiguration;

    @Override
    public boolean recordLocation(@Nonnull EntityType type, @Nonnull Long targetId, @Nonnull String ip, Double lat, Double lng) throws ServiceException {
        if (lat == null || lng == null) {
            ResponseBody responseBody = analysisAddress(ip);
            if (!isSuccess(responseBody)) {
                return false;
            }
            PlaceLocationDescription locationDescription = Json.readValue(responseBody.getData(), PlaceLocationDescription.class);
            Assert.notNull(locationDescription);
            Location location = locationDescription.getLocation();
            Assert.notNull(location);
            lat = location.getLat();
            lng = location.getLng();
        }
        ResponseBody responseBody = analysisAddress(lat, lng);
        if (!isSuccess(responseBody)) {
            return false;
        }
        locationRecorder.newRecord(LocationProvider.TENCENT_MAP, type, targetId, ip, Json.readValue(responseBody.getData(), PlaceLocationDescription.class));
        return true;
    }

    @Nonnull
    @Override
    public LocationDescription[] suggestion(@Nonnull String ip, Double lat, Double lng) throws ServiceException {
        if (lat == null || lng == null) {
            ResponseBody responseBody = analysisAddress(ip);
            if (!isSuccess(responseBody)) {
                return new LocationDescription[]{};
            }
            PlaceLocationDescription locationDescription = Json.readValue(responseBody.getData(), PlaceLocationDescription.class);
            Assert.notNull(locationDescription);
            Location location = locationDescription.getLocation();
            Assert.notNull(location);
            lat = location.getLat();
            lng = location.getLng();
        }
        ResponseBody responseBody = analysisSuggestion(lat, lng);
        if (!isSuccess(responseBody)) {
            return new LocationDescription[]{};
        }
        LocationDescription[] LocationDescription = Json.readValue(responseBody.getData(), PlaceLocationDescription[].class);
        Assert.notNull(LocationDescription);
        return LocationDescription;
    }

    @Override
    public ResponseBody analysisAddress(String ip) {
        return new LocationResponseBody(this.getRestTemplate().getForObject(locationConfiguration.getIpLocation(), String.class, ip));
    }

    @Override
    public <T extends ResponseBody> T analysisAddress(Double lat, Double lng) {
        return (T) new LocationResponseBody(this.getRestTemplate().getForObject(locationConfiguration.getGeoLocation(), String.class, lat, lng, EXCLUDE_POI));
    }

    public ResponseBody analysisSuggestion(Double lat, Double lng) {
        return new SuggestResponseBody(this.getRestTemplate().getForObject(locationConfiguration.getGeoLocation(), String.class, lat, lng, INCLUDE_POI));
    }

    private RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    private boolean isSuccess(ResponseBody responseBody) {
        return responseBody.isSuccess() && responseBody.getData() != null;
    }

}
