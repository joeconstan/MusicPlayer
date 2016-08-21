package com.example.joseph.musicplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;



//this is the screen that appears when you click on a song in the listview. an individual song screen.
public class Songscreen extends Activity {


    public static final String TAG = MainActivity.class.getSimpleName();
    boolean playing = false;
    boolean prepared = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_screen);
        //Toolbar mToolbar = (Toolbar) findViewById(R.id.m_toolbar);
        //setSupportActionBar(mToolbar);


        Intent intent = getIntent();
        String songName = intent.getStringExtra("songTitle");
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        final ImageButton playButton = (ImageButton) findViewById(R.id.playButtonSong);
        final ImageButton fwdButton = (ImageButton) findViewById(R.id.forwButtonSong);
        final ImageButton backButton = (ImageButton) findViewById(R.id.backwButtonSong);

        final ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.Audio.Media.DATA};
        final Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
        final Uri mySongUri = uri;
        try {
            mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.v(TAG, "about to start");
                mediaPlayer.start();
                prepared = true;
                playing = true;
            }
        });




        if (playButton != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (playing) {
                            Log.v(TAG, "about to stop");
                            mediaPlayer.pause();
                            playing = false;
                            int id = getResources().getIdentifier("play_butt_white", "mipmap", getPackageName());
                            playButton.setImageResource(id);
                        } else {
                            int id = getResources().getIdentifier("pause_butt_white", "mipmap", getPackageName());
                            playButton.setImageResource(id);
                            if (!prepared) {
                                mediaPlayer.prepareAsync();
                            } else {
                                mediaPlayer.start();
                            }
                            playing = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


         //Forward button
        if (fwdButton != null)
        {
            fwdButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    try
                    {
                        if (playing)
                        {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.reset();
                        cursor.moveToNext();
                        long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
                        final Uri mySongUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
                        mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                        int pic = getResources().getIdentifier("pause_butt_white", "mipmap", getPackageName());
                        playButton.setImageResource(pic);

                        mediaPlayer.prepareAsync();

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        //back button
        if (backButton != null)
        {
            backButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    try
                    {
                        if (playing)
                        {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.reset();

                        cursor.moveToPrevious();

                        if(cursor.isBeforeFirst())
                        {
                            int pic = getResources().getIdentifier("play_butt_white", "mipmap", getPackageName());
                            playButton.setImageResource(pic);
                            cursor.moveToNext();
                        }
                        long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
                        final Uri mySongUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
                        mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                        int pic = getResources().getIdentifier("pause_butt_white", "mipmap", getPackageName());
                        playButton.setImageResource(pic);

                        mediaPlayer.prepareAsync();

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        final Handler mHandler = new Handler();

        /*final SeekBar seekBar = (SeekBar)findViewById(R.id.seek_bar);
        seekBar.setMax(mediaPlayer.getDuration());

        Songscreen.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }

        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        }); */







    }
}
