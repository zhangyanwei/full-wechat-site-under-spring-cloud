package com.askdog.web.api.generic;

import com.askdog.service.admin.IndexService;
import com.askdog.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/mgr")
public class IndexApi {

    @Autowired
    private IndexService indexService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/indices/{type}", method = PUT)
    public void rebuildIndices(@PathVariable("type") String type) throws ServiceException {
        indexService.rebuildAll(type);
    }

}
