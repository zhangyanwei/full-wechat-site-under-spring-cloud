package com.askdog.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("service")
@RequestMapping("/service/token")
public interface TokenService {

    @Nonnull
    @RequestMapping(value = "/{name}/{token}", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    Map<String, String> tokenDetail(@PathVariable("name") String tokenName, @PathVariable("token") String token);

}
