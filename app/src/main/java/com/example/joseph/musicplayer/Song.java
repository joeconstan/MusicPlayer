package com.example.joseph.musicplayer;


import android.graphics.Bitmap;
import android.net.Uri;

public class Song
{
    private String title;
    private String artist;
    private Bitmap albumArt;
    private String track;
    private Uri uri;
    private boolean playing; //better way to do this?

    public Song(){
        playing = false;
    }
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

    public Uri getUri(){return uri;}

    public boolean getPlaying(){return playing;}


    public void setPlaying(boolean playing){this.playing = playing;}
    public void setUri(Uri uri){this.uri = uri;}
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
