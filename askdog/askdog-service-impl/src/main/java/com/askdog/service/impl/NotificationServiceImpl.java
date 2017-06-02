package com.askdog.service.impl;

import com.askdog.dao.repository.mongo.OriginalNotificationRepository;
import com.askdog.model.business.Notification;
import com.askdog.model.data.OriginalNotification;
import com.askdog.model.data.inner.NotificationType;
import com.askdog.service.MessageService;
import com.askdog.service.NotificationService;
import com.askdog.service.bo.PureNotification;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.notification.NotificationHelper;
import com.askdog.service.impl.notification.NotificationRecipientResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.service.bo.common.PagedDataUtils.rePage;
import static com.askdog.service.exception.NotFoundException.Error.NOTIFICATION;

@Service
@RestController
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired private MessageService messageService;
    @Autowired private NotificationHelper notificationHelper;
    @Autowired private NotificationRecipientResolver notificationRecipientResolver;
    @Autowired private OriginalNotificationRepository originalNotificationRepository;

    @Nonnull
    @Override
    public Notification save(@RequestBody PureNotification pureNotification) {

        String logId = pureNotification.getLogId();
        NotificationType notificationType = pureNotification.getNotificationType();

        OriginalNotification on = originalNotificationRepository.findByNotificationTypeAndLogId(notificationType, logId);
        checkState(on == null, () -> new ConflictException(ConflictException.Error.NOTIFICATION));

        OriginalNotification originalNotification = new OriginalNotification();
        originalNotification.setNotificationType(notificationType);
        originalNotification.setRecipient(notificationRecipientResolver.resolve(notificationType, logId));
        originalNotification.setLogId(logId);
        originalNotification.setRead(false);
        originalNotification.setCreateDate(new Date());

        OriginalNotification savedOriginalNotification = originalNotificationRepository.save(originalNotification);

        Notification notification = notificationHelper.convert(savedOriginalNotification);
        messageService.sendNotificationMessage(notification);

        return notification;
    }

    @Nonnull
    @Override
    public PagedData<Notification> findAll(@Nonnull @PathVariable("userId") Long userId,
                                           @Nonnull @RequestBody Pageable pageable) {
        Page<OriginalNotification> notifications = originalNotificationRepository.findByRecipient(userId, pageable);

        notifications.getContent()
                .stream()
                .filter(originalNotification -> !originalNotification.isRead())
                .forEach(originalNotification -> {
                    markAsRead(originalNotification.getId());
                    originalNotification.setRead(true);
                });

        return rePage(notifications, pageable, t -> notificationHelper.convert(t));
    }

    @Nonnull
    @Override
    public List<Notification> findPreviewNotifications(@Nonnull @PathVariable("userId") Long userId) {
        return originalNotificationRepository.findTop10ByRecipientAndReadFalseOrderByCreateDateDesc(userId)
                .stream().map(t -> notificationHelper.convert(t)).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(@Nonnull @PathVariable("notificationId") String notificationId) {
        OriginalNotification originalNotification = findExists(notificationId);
        originalNotification.setRead(true);
        originalNotificationRepository.save(originalNotification);
    }

    @Override
    public long findNoticeCount(@Nonnull @PathVariable("userId") Long userId) {
        return originalNotificationRepository.countByRecipientAndReadFalse(userId);
    }

    @Override
    public void readAll(@Nonnull @PathVariable("userId") Long userId) {
        originalNotificationRepository.readAll(userId);
    }

    private OriginalNotification findExists(@Nonnull String notificationId) {
        return originalNotificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException(NOTIFICATION));
    }
}
