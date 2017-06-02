package com.askdog.service;

import com.askdog.model.business.Notification;
import com.askdog.service.bo.PureNotification;
import com.askdog.service.bo.common.PagedData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Nonnull;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@FeignClient("service")
@RequestMapping("/service/notifications")
public interface NotificationService {

    @Nonnull
    @RequestMapping(method = POST)
    Notification save(@RequestBody PureNotification pureNotification);

    @Nonnull
    @RequestMapping(value = "/user/{userId}")
    PagedData<Notification> findAll(@Nonnull @PathVariable("userId") Long userId,
                                    @Nonnull @RequestBody Pageable pageable);

    @Nonnull
    @RequestMapping(value = "/preview/user/{userId}")
    List<Notification> findPreviewNotifications(@Nonnull @PathVariable("userId") Long userId);

    @RequestMapping(value = "/read/{notificationId}", method = PUT)
    void markAsRead(@Nonnull @PathVariable("notificationId") String notificationId);

    @RequestMapping(value = "/user/{userId}/count")
    long findNoticeCount(@Nonnull @PathVariable("userId") Long userId);

    @RequestMapping(value = "/read/{userId}/all", method = PUT)
    void readAll(@Nonnull @PathVariable("userId") Long userId);
}
