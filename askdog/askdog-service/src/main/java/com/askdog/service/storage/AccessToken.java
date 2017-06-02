package com.askdog.service.storage;

import com.askdog.service.storage.aliyun.OssAccessToken;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OssAccessToken.class, name = "OssAccessToken")})
public interface AccessToken {
}
