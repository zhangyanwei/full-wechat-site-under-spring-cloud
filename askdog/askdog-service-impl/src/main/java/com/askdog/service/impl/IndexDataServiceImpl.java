package com.askdog.service.impl;

import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.service.IndexDataService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.exception.ServiceException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.askdog.model.common.EventType.EventTypeGroup.valueOf;

@RestController
public class IndexDataServiceImpl implements IndexDataService {

    @Override
    public PagedData<Long> retrieve(@PathVariable("type") String type,
                                    @RequestParam("page") int page,
                                    @RequestParam("size") int size) throws ServiceException {

        EventTypeGroup eventTypeGroup = valueOf(type);
        return null;
    }
}
