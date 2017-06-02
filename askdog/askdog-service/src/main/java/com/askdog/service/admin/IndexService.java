package com.askdog.service.admin;

import com.askdog.service.exception.ServiceException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("service")
@RequestMapping("/service/indices")
public interface IndexService {

    @RequestMapping(value = "/type/{type}", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    void rebuildAll(@PathVariable("type") String type) throws ServiceException;

}
