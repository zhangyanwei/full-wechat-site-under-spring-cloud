package com.askdog.web.api.generic;

import com.askdog.model.data.inner.ResourceType;
import com.askdog.service.StorageService;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.storage.AccessToken;
import com.askdog.service.storage.StorageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.askdog.common.utils.Json.writeValueAsString;
import static com.askdog.web.utils.AliyunOSSUtils.verifyOSSCallbackRequest;
import static com.google.common.base.Preconditions.checkState;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/storage")
public class StorageApi {

    private static final String RESOURCE_ID_KEY = "resourceId";

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/access_token", method = GET)
    public AccessToken accessToken(@RequestParam("type") @Nonnull ResourceType type, @RequestParam("extention") @Nonnull String extention) throws ServiceException {
        return storageService.generateAccessToken(type, extention);
    }

    @RequestMapping(value = "/callback", method = POST)
    public StorageResource callback(HttpServletRequest request, @RequestBody Map<String, Object> map) throws ServiceException, UnsupportedEncodingException {
        checkState(verifyOSSCallbackRequest(request, writeValueAsString(map)));
        return storageService.persistResource(Long.valueOf(String.valueOf(map.get(RESOURCE_ID_KEY))));
    }

}
