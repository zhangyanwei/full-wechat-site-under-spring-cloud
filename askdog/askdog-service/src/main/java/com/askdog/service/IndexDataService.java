package com.askdog.service;

import com.askdog.service.bo.common.PagedData;
import com.askdog.service.exception.ServiceException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("service")
@RequestMapping("/service/indices_data")
public interface IndexDataService {

    @RequestMapping(value = "/type/{type}", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    PagedData<Long> retrieve(@PathVariable("type") String type, @RequestParam("page") int page, @RequestParam("size") int size) throws ServiceException;

}
