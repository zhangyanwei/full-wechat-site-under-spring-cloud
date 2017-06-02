package com.askdog.service.impl.storage.aliyun;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

import static com.askdog.common.utils.SpelUtils.parse;

public class OssConfiguration {

    @NotNull
    private String endpoint;

    @NotNull
    private String accessId;

    @NotNull
    private String accessKey;

    @NotNull
    private String bucket;

    @NotNull
    private Policy policy;

    private String urlAlias;

    private Callback callback;

    private Mns mns;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Mns getMns() {
        return mns;
    }

    public void setMns(Mns mns) {
        this.mns = mns;
    }

    public String getHost() {
        return "http://" + bucket + "." + endpoint;
    }

    public static class Policy {

        private String expireTime;

        private String maxSize;

        @NotNull
        private String baseDir;

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = parse(expireTime);
        }

        public String getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(String maxSize) {
            this.maxSize = parse(maxSize);
        }

        public String getBaseDir() {
            return baseDir;
        }

        public void setBaseDir(String baseDir) {
            this.baseDir = baseDir;
        }
    }

    public static class Callback implements Cloneable {

        @NotNull
        @JsonProperty("callbackUrl")
        private String url;

        @NotNull
        @JsonProperty("callbackHost")
        private String host;

        @NotNull
        @JsonProperty("callbackBody")
        private String body;

        @NotNull
        @JsonProperty("callbackBodyType")
        private String bodyType;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        @Override
        public Callback clone() {
            try {
                return (Callback) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Mns {

        @NotNull
        private String url;

        @NotNull
        private String queueName;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }
    }
}
