package com.example.joseph.musicplayer;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MusicService extends Service {

    private static final String TAG = "MusicService";

    @Override
    public void onCreate() {
        //create media player here
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setDataSource(); pass as intent extras

        mediaPlayer.prepareAsync();
        //mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }





}
