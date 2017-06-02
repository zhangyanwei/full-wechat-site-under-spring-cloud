package com.askdog.service.impl.admin;

import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.service.MessageService;
import com.askdog.service.admin.IndexService;
import com.askdog.service.exception.ServiceException;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class IndexServiceImpl implements IndexService {

    @Autowired
    private MessageService messageService;

    @Override
    public void rebuildAll(@PathVariable("type") String type) throws ServiceException {
        EventTypeGroup group = EventTypeGroup.valueOf(Strings.toUpperCase(type));
        // TODO send the event to rebuild the indices.
        //messageService.sendIndexMessage(REBUILD_ALL_INDEX_EXPERIENCE, null);
    }
}
