package com.askdog.service;

import com.askdog.model.data.ExceptionLog;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("service")
@RequestMapping("/service/exception_logs")
public interface ExceptionLogService {

    @Nonnull
    @RequestMapping(method = POST)
    ExceptionLog save(@Valid @RequestBody ExceptionLog exceptionLog);
}
