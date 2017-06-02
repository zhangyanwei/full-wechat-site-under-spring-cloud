package com.askdog.service.impl;

import com.askdog.dao.repository.mongo.ExceptionLogRepository;
import com.askdog.model.data.ExceptionLog;
import com.askdog.service.ExceptionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;

@RestController
public class ExceptionLogServiceImpl implements ExceptionLogService {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    @Nonnull
    @Override
    public ExceptionLog save(@Valid @RequestBody ExceptionLog exceptionLog) {
        return exceptionLogRepository.save(exceptionLog);
    }
}