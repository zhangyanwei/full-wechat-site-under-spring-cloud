package com.askdog.service.impl.location.tencent;

import com.askdog.common.utils.Json;
import com.askdog.service.location.ResponseBody;

import javax.annotation.Nonnull;

import static com.askdog.service.impl.location.tencent.LocationResponseBody.ResponseBodyNode.*;

public class LocationResponseBody implements ResponseBody {

    public enum ResponseBodyNode {
        STATUS, MESSAGE, RESULT, POIS
    }

    private Integer status;
    private String message;
    private String data;

    public LocationResponseBody(@Nonnull String responseBody) {
        this.status = Integer.parseInt(Json.readNode(responseBody, STATUS.name().toLowerCase()));
        this.message = Json.readNode(responseBody, MESSAGE.name().toLowerCase());
        if (isSuccess()) {
            this.data = Json.readNode(responseBody, RESULT.name().toLowerCase());
        }
    }

    @Override
    public boolean isSuccess() {
        return !(this.status == null || this.status != 0);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getData() {
        return this.data;
    }

    protected void setData(@Nonnull String data) {
        this.data = data;
    }

}

