package com.askdog.model.data.video;

import java.io.Serializable;

public class VideoItem implements Serializable {

    private static final long serialVersionUID = -4833265004382926245L;

    private long width;
    private long height;
    private float duration;
    private String format;
    private float fileSize;
    private float bitRate;
    private String url;
    private Definition definition;

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public float getFileSize() {
        return fileSize;
    }

    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }

    public float getBitRate() {
        return bitRate;
    }

    public void setBitRate(float bitRate) {
        this.bitRate = bitRate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Definition getDefinition() {
        return definition;
    }

    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    public enum Definition {
        UHD("超清"), FHD("全高清"), HD("高清"), SD("标清"), LD("流畅");

        private String name;

        Definition(String name) {
            this.name = name;
        }

        public static Definition parse(int resolutionWidth) {
            if (resolutionWidth >= 1280) { return HD; }
            if (resolutionWidth >= 848) { return SD; }
            return LD;
        }
    }
}
