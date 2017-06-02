package com.askdog.service.impl.storage.aliyun.description;

import com.askdog.model.data.video.Video;

public class OssVideoResourceDescription extends OssResourceDescription {

    private static final long serialVersionUID = -3428745187561361800L;

    public OssVideoResourceDescription() {
    }

    public OssVideoResourceDescription(OssResourceDescription ossResourceDescription, Video video) {
        this.setEndpoint(ossResourceDescription.getEndpoint());
        this.setBucket(ossResourceDescription.getBucket());
        this.setPersistenceName(ossResourceDescription.getPersistenceName());
        this.setUrlAlias(ossResourceDescription.getUrlAlias());
        this.video = video;
    }

    private Video video;

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
