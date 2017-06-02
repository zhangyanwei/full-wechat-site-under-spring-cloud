package com.askdog.service.impl.storage.aliyun.description;

import com.askdog.model.data.inner.ResourceDescription;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;

import static java.lang.String.format;
import static org.elasticsearch.common.Strings.isNullOrEmpty;

public class OssResourceDescription extends ResourceDescription {

    @NotNull
    private String endpoint;

    @NotNull
    private String bucket;

    @NotNull
    private String persistenceName;

    private String urlAlias;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPersistenceName() {
        return persistenceName;
    }

    public void setPersistenceName(String persistenceName) {
        this.persistenceName = persistenceName;
    }

    public String getUrlAlias() {
        return urlAlias;
    }

    public void setUrlAlias(String urlAlias) {
        this.urlAlias = urlAlias;
    }

    @Override public String getResourceUrl() {
        String formatHost = "http://%s/" + getPersistenceName();
        String urlAlias = getUrlAlias();
        if (!isNullOrEmpty(urlAlias)) {
            return format(formatHost, urlAlias);
        }

        return format(formatHost, getBucket() + "." + getEndpoint());
    }
}
