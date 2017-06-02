package com.askdog.service.bo.storedetail;

import com.askdog.common.Out;
import com.askdog.model.data.video.Video;

public class StorePageDetail_Video implements Out<StorePageDetail_Video, Video> {

    private float duration;
    private String snapshot;

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public StorePageDetail_Video from(Video entity) {
        StorePageDetail_Video _storeDetailVideo = new StorePageDetail_Video();
        _storeDetailVideo.setDuration(entity.getSource().getDuration());

        if (entity.getSnapshots() != null && entity.getSnapshots().size() > 0) {
            _storeDetailVideo.setSnapshot(entity.getSnapshots().get(0).getUrl());
        }

        return _storeDetailVideo;
    }
}
