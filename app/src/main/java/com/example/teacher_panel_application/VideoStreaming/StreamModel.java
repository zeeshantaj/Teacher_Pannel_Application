package com.example.teacher_panel_application.VideoStreaming;

public class StreamModel {
    String streamerName,liveId,title,imageUrl,time,userId;
    boolean live;

    public StreamModel(String streamerName, String liveId, String title, String imageUrl, String time, String userId, boolean live) {
        this.streamerName = streamerName;
        this.liveId = liveId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.time = time;
        this.userId = userId;
        this.live = live;
    }

    public StreamModel() {
    }

    public String getStreamerName() {
        return streamerName;
    }

    public void setStreamerName(String streamerName) {
        this.streamerName = streamerName;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
