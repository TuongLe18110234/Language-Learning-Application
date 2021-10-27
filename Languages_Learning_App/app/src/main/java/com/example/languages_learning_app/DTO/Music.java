package com.example.languages_learning_app.DTO;

import com.example.languages_learning_app.Common.Common;

public class Music {
    private int musicId, languageId;
    private String videoname, url, type;

    public Music(int languageId, int musicId, String videoname, String url, String type) {
        this.languageId = languageId;
        this.videoname = videoname;
        this.musicId = musicId + 1;
        this.type = type;
        this.url = url;
    }

    public Music() {

    }

    public int getMusicId() { return musicId; }

    public void setMusicId(int musicId) { this.musicId = musicId; }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getVideoname() { return videoname; }

    public void setVideoname(String videoname) { this.videoname = videoname; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}
