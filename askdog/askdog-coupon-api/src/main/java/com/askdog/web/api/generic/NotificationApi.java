package com.askdog.web.api.generic;

import com.askdog.model.business.Notification;
import com.askdog.service.NotificationService;
import com.askdog.service.bo.common.GroupDate;
import com.askdog.service.bo.common.GroupedData;
import com.askdog.service.bo.common.PagedData;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.askdog.service.bo.common.GroupDataResultHelper.group;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/notifications")
public class NotificationApi {

    @Autowired private NotificationService notificationService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping
    public PagedData<GroupedData<Notification>> findAll(@AuthenticationPrincipal AdUserDetails user,
                                                        @PageableDefault(sort = "createDate", direction = DESC) Pageable pageable) {
        return build(notificationService.findAll(user.getId(), pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/preview")
    public List<Notification> findPreviewNotifications(@AuthenticationPrincipal AdUserDetails user) {
        return notificationService.findPreviewNotifications(user.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/read_all", method = PUT)
    public void readAll(@AuthenticationPrincipal AdUserDetails user) {
        notificationService.readAll(user.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}/read", method = PUT)
    public void markAsRead(@PathVariable("id") String notificationId) {
        notificationService.markAsRead(notificationId);
    }

    private PagedData<GroupedData<Notification>> build(PagedData<Notification> notifications) {
        List<GroupedData<Notification>> groupedData = group(notifications.getResult(), notification -> {
            LocalDate date = notification.getCreateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return new GroupDate().setY(date.getYear()).setM(date.getMonth().getValue()).setD(date.getDayOfMonth());
        });
        return new PagedData<>(groupedData, notifications.getSize(), notifications.getPage(), notifications.getTotal(), notifications.isLast());
    }
}