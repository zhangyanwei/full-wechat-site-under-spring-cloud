package com.askdog.dao.repository.mongo;

import com.askdog.dao.repository.mongo.extention.OriginalNotificationRepositoryExtention;
import com.askdog.model.data.OriginalNotification;
import com.askdog.model.data.inner.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OriginalNotificationRepository extends MongoRepository<OriginalNotification, String>, OriginalNotificationRepositoryExtention {

    Optional<OriginalNotification> findById(String notificationId);

    Page<OriginalNotification> findByRecipient(Long recipient, Pageable pageable);

    List<OriginalNotification> findTop10ByRecipientAndReadFalseOrderByCreateDateDesc(Long recipient);

    long countByRecipientAndReadFalse(Long recipient);

    OriginalNotification findByNotificationTypeAndLogId(NotificationType notificationType, String logId);
}
