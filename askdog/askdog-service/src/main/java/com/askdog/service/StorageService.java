package com.askdog.service;

import com.askdog.model.data.inner.ResourceType;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.storage.AccessToken;
import com.askdog.service.storage.StorageResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Nonnull;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("service")
@RequestMapping("/service/storage")
public interface StorageService {

    @Nonnull
    @RequestMapping(method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    AccessToken generateAccessToken(@RequestParam("type") @Nonnull ResourceType type, @RequestParam("extention") @Nonnull String extention) throws ServiceException;

    @Nonnull
    @RequestMapping(method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    StorageResource persistResource(@RequestParam("resourceId") @Nonnull Long resourceId) throws ServiceException;

}
