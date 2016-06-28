/* bugs
close the cursor
handle isplaying problems
setdatasource is commented out in the player button listener
*/

/*
display artist
find a place for this list
choose/start/stop by cclicking song
stop button
skip and back button
album
search
other screen for individ song
album artwork

playlists
other tabs (playlist, album, artist)
shuffle
loop

design - fernando
 */



package com.example.joseph.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;



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

        ListAdapter listAdapter = new songAdapter(this, songs);
        final ListView listView = (ListView) findViewById(R.id.listView);


        listView.setAdapter(listAdapter);

        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {


                    if (playing) {
                        Log.v(TAG, "about to stop");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        //mediaPlayer.release();
                        playing = false;
                    }
                    cursor.moveToPosition(position);
                    long mySongId=cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
                    final Uri mySongUri=ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
                    mediaPlayer.setDataSource(getApplicationContext(), mySongUri);

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                Log.v(TAG, "about to start");
                                mediaPlayer.start();
                                playing = true;
                            }
                        });
                        mediaPlayer.prepareAsync();

                }catch(Exception e) {e.printStackTrace();}
            }
        });


        //final ImageButton playButton = (ImageButton) findViewById(R.id.playButton);

       /* if (playButton != null) {
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
                            int imageID = getResources().getIdentifier("com.example.joseph.music:drawable/play_button", null, null);
                            playButton.setImageResource(imageID);
                        } else {

//                          mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                            mediaPlayer.prepare();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mediaPlayer.start();
                                    playing = true;
                                }
                            });
                            int imageID2 = getResources().getIdentifier("com.example.joseph.music:drawable/pause_button", null, null);
                            playButton.setImageResource(imageID2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
            });
        }
*/    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }


}




