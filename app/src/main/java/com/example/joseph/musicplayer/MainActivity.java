package com.example.joseph.musicplayer;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    boolean playing = false; //--------------------------------------------------------
    public boolean queueSongs(Cursor cursor, Song songs[]){
        if (cursor.moveToFirst()) { //needs to check for first element to avoid nullptr exception
            for (int j=0;j<21;j++){
                songs[j].setTitle(cursor.getString(1));
                songs[j].setTrack(cursor.getString(2));
                cursor.moveToNext();
            }
        }


      return true;
    }


    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.m_toolbar);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= 23) {
                boolean f = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (f)
                    Log.v(TAG,"Permission is granted");
                else {
                    Log.v(TAG, "Permission not granted");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }

        final ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.Audio.Media.DATA};
        final Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        Song songs[] = new Song[21];
        for (int i=0; i<21;i++)
            songs[i] = new Song();
        queueSongs(cursor, songs);
        cursor.moveToFirst();
        cursor.moveToNext();
        cursor.moveToNext();
        cursor.moveToNext();
        cursor.moveToNext();
        cursor.moveToNext();
        ListAdapter listAdapter = new songAdapter(this, songs);
        final ListView listView = (ListView) findViewById(R.id.listView);
        long mySongId=cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
        final Uri mySongUri=ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
        cursor.close();
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //this function should only have to find
                Log.v(TAG, "URI PATH: " + mySongUri.getPath());          //the song in an array or vector and play it from there, w/o a cursor
                try {
                    final MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                    mediaPlayer.prepareAsync();
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    else {
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                            }
                        });

                    }
                }catch(Exception e) {e.printStackTrace();}
            }
        });

        final ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        final MediaPlayer mediaPlayer = new MediaPlayer();

        if (playButton != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                            //mediaPlayer.reset();
                            //mediaPlayer.release();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        //mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                        Log.v(TAG, "state: " + playing);

                        if (playing) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            playing = false;
                            int imageID = getResources().getIdentifier("com.example.joseph.musicplayer:drawable/ic_play", null, null);
                            playButton.setImageResource(imageID);
                        } else {

                            mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                            mediaPlayer.prepare();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mediaPlayer.start();
                                    playing = true;
                                }
                            });
                            int imageID2 = getResources().getIdentifier("com.example.joseph.musicplayer:drawable/ic_pause", null, null);
                            playButton.setImageResource(imageID2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }


}




