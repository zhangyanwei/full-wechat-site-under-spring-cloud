package com.askdog.model.data;

import com.askdog.model.data.inner.NotificationType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "mc_notification")
public class OriginalNotification extends Base {

    private static final long serialVersionUID = 1048263021333844017L;

    private NotificationType notificationType;
    private Long recipient;
    private String logId;
    private boolean read;
    private Date createDate;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(Long recipient) {
        this.recipient = recipient;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
