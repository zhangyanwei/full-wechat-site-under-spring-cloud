package com.askdog.wechat.api.wxclient.model;

import com.askdog.wechat.api.WxObject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class TemplateNotice implements WxObject {

    private TemplateNotice() {
    }

    private TemplateNotice(Builder builder) {
        this.toUser = builder.toUser;
        this.templateId = builder.templateId;
        this.topColor = builder.topColor;
        this.url = builder.url;
        this.data = builder.data;
        this.targetId = builder.targetId;
    }

    @JsonProperty(value = "touser")
    private String toUser;

    @JsonProperty(value = "template_id")
    private String templateId;

    @JsonProperty(value = "topcolor")
    private String topColor;

    @JsonProperty(value = "url")
    private String url;

    @JsonProperty(value = "data")
    private Map<String, DataValue> data;

    private long targetId;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTopColor() {
        return topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, DataValue> getData() {
        return data;
    }

    public void setData(Map<String, DataValue> data) {
        this.data = data;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public static class Builder {

        private String toUser;

        private String templateId;

        private String topColor;

        private String url;

        private Map<String, DataValue> data;

        private long targetId;

        public Builder toUser(String toUser) {
            this.toUser = toUser;
            return this;
        }

        public Builder templateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Builder topColor(String topColor) {
            this.topColor = topColor;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder data(Map<String, DataValue> data) {
            this.data = data;
            return this;
        }

        public Builder targetId(long targetId) {
            this.targetId = targetId;
            return this;
        }

        public TemplateNotice build() {
            return new TemplateNotice(this);
        }
    }

    public static class DataValue {

        private String value;
        private String color;

        public DataValue(String value, String color) {
            this.value = value;
            this.color = color;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
