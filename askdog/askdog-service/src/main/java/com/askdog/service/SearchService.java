package com.askdog.service;

import com.askdog.service.search.SearchRequest;
import com.askdog.service.search.SearchResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Nonnull;

@FeignClient("service")
@RequestMapping("/service")
public interface SearchService {

    @RequestMapping(value = "/experience/similar/search", method = RequestMethod.POST)
    SearchResult similar(@RequestBody @Nonnull SearchRequest searchRequest);
}
