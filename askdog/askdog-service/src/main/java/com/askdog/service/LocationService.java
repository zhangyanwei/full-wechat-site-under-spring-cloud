package com.askdog.service;

import com.askdog.model.data.inner.EntityType;
import com.askdog.model.data.inner.location.LocationDescription;
import com.askdog.service.exception.ServiceException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/location")
public interface LocationService {

    @RequestMapping(value = "/record", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    boolean recordLocation(
            @Nonnull @RequestParam("type") EntityType type,
            @RequestParam("targetId") @Nonnull Long targetId,
            @RequestParam("ip") @Nonnull String ip,
            @RequestParam(name = "lat", required = false) Double lat,
            @RequestParam(name = "lng", required = false) Double lng) throws ServiceException;

    @Nonnull
    @RequestMapping(value = "/records", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    LocationDescription[] suggestion(@RequestParam("ip") @Nonnull String ip, @RequestParam("lat") Double lat, @RequestParam("lng") Double lng) throws ServiceException;

    @RequestMapping(value = "/user/{userId}/residence", method = PUT)
    void updateUserResidence(@Nonnull @PathVariable("userId") Long userId) throws ServiceException;

}
