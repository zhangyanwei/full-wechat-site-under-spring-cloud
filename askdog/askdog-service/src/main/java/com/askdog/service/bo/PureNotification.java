package com.askdog.service.bo;

import com.askdog.model.data.inner.NotificationType;

import javax.validation.constraints.NotNull;

public class PureNotification {

    @NotNull
    private NotificationType notificationType;

    @NotNull
    private String logId;

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
