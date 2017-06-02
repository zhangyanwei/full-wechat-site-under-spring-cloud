package com.askdog.model.data.video;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {

    private static final long serialVersionUID = -469491468133597786L;
    
    private VideoItem source;
    private List<VideoSnapshot> snapshots = Lists.newArrayList();
    private List<VideoItem> transcodeVideos = Lists.newArrayList();
    private List<FailedInfo> failures = Lists.newArrayList();

    public VideoItem getSource() {
        return source;
    }

    public void setSource(VideoItem source) {
        this.source = source;
    }

    public List<VideoSnapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<VideoSnapshot> snapshots) {
        this.snapshots = snapshots;
    }

    public List<VideoItem> getTranscodeVideos() {
        return transcodeVideos;
    }

    public void setTranscodeVideos(List<VideoItem> transcodeVideos) {
        this.transcodeVideos = transcodeVideos;
    }

    public List<FailedInfo> getFailures() {
        return failures;
    }

    public void setFailures(List<FailedInfo> failures) {
        this.failures = failures;
    }
}