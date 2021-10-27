package com.example.languages_learning_app.DTO;

import java.io.Serializable;

public class YouTubeVideo implements Serializable {

    private String id;
    private String songName;
    private String singer;
    private String videoId;
    private String lyric;

    public YouTubeVideo() {

    }

    public YouTubeVideo(String id, String songName, String singer, String videoId, String lyric) {
        this.id = id;
        this.songName = songName;
        this.singer = singer;
        this.videoId = videoId;
        this.lyric = lyric;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }
}
