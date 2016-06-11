package com.example.joseph.musicplayer;


public class Song
{
    private String title;
    private String artist;
    //private String Album;
    private String track;

    public Song getSong(){
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getTrack() {
        return track;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setTrack(String track) {
        this.track = track;
    }
}
