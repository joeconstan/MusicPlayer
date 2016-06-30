package com.example.joseph.musicplayer;


import android.graphics.Bitmap;

public class Song
{
    private String title;
    private String artist;
    private Bitmap albumArt;
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

    public Bitmap getAlbumArt(){return albumArt; }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setAlbumArt(Bitmap art){this.albumArt = art; }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTrack(String track) {
        this.track = track;
    }
}
