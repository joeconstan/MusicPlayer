package com.example.joseph.musicplayer;


import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();


    public boolean queueSongs(Cursor cursor, Song songs[]){


        if (cursor.moveToFirst()) {
            for (int j=0;j<21;j++) {

                songs[j].setTitle(cursor.getString(1));
                songs[j].setTrack(cursor.getString(2));

                Log.v(TAG, "path: " + songs[j].getTrack());
                long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
                Uri mySongUr = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
                songs[j].setUri(mySongUr);
                metaRetriever.setDataSource(this.getApplicationContext(), mySongUr);
                String artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                songs[j].setArtist(artist);
                cursor.moveToNext();

            }
        }

        return true;
    }




    @Override
    public void onCreate() {
        final ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.Audio.Media.DATA};
        final Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        final Song songs[] = new Song[21];
        for (int i = 0; i < 21; i++)
            songs[i] = new Song();
        queueSongs(cursor, songs);
        cursor.close();

        //create media player here
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setDataSource(); pass as intent extras

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();

                for (int i=0;i<21;i++){ //find out which song is playing
                    //if (songs[i].getPlaying())
                        //titlePanelText.setText(songs[i].getTitle());
                }

                //prepared = true;
                //playing = true;
            }
        });

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
