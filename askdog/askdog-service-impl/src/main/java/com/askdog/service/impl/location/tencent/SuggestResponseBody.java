package com.askdog.service.impl.location.tencent;

import com.askdog.common.utils.Json;

import javax.annotation.Nonnull;

import static com.askdog.service.impl.location.tencent.LocationResponseBody.ResponseBodyNode.POIS;

public class SuggestResponseBody extends LocationResponseBody {

    public SuggestResponseBody(@Nonnull String responseBody) {
        super(responseBody);
        if (isSuccess()) {
            this.setData(Json.readNode(this.getData(), POIS.name().toLowerCase()));
        }
    }
}
