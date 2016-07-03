




package com.example.joseph.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
    boolean playing = false;
    boolean prepared = false;

    public boolean queueSongs(Cursor cursor, Song songs[]){
        if (cursor.moveToFirst()) {
            for (int j=0;j<21;j++){
                songs[j].setTitle(cursor.getString(1));
                songs[j].setTrack(cursor.getString(2));
                Log.v(TAG, "path: " + songs[j].getTrack());
                long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
                Uri mySongUr = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
                metaRetriever.setDataSource(this.getApplicationContext(), mySongUr);
                String artist =  metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                songs[j].setArtist(artist);

                metaRetriever.setDataSource(songs[j].getTrack());
                byte[] arr = metaRetriever.getEmbeddedPicture();
                if (arr==null){
                    Log.v(TAG, "TAG: its null bro");
                }
                else {
                    InputStream is = new ByteArrayInputStream(arr);
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    songs[j].setAlbumArt(bm);
                }

/*
                Bitmap bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 , blob);
                //byte[] bitmapdata = blob.toByteArray();

                songs[j].setAlbumArt(bitmap);
*/
                //path = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM_ART));

                //Bitmap bm = getImage(path);
                //songs[j].setAlbumArt(bm);

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
                Log.v(TAG, "Permission is granted");
            else {
                Log.v(TAG, "Permission not granted");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        final ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.Audio.Media.DATA};
        final Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        Song songs[] = new Song[21];
        for (int i = 0; i < 21; i++)
            songs[i] = new Song();
        queueSongs(cursor, songs);

        ListAdapter listAdapter = new songAdapter(this, songs);
        final ListView listView = (ListView) findViewById(R.id.listView);

        final ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        listView.setAdapter(listAdapter);
        cursor.moveToFirst();

        final ImageButton fwdButton = (ImageButton) findViewById(R.id.forwButton);
        final ImageButton backButton = (ImageButton) findViewById(R.id.backwButton);

        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
        final Uri mySongUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (playing) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.reset();
                    cursor.moveToPosition(position);
                    long mySongId = cursor.getLong(cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID));
                    final Uri mySongUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mySongId);
                    mediaPlayer.setDataSource(getApplicationContext(), mySongUri);
                    int pic = getResources().getIdentifier("pause_butt_white", "mipmap", getPackageName());
                    playButton.setImageResource(pic);

                    mediaPlayer.prepareAsync();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(view.getContext(), Songscreen.class);
                String songName = cursor.getString(1);
                intent.putExtra("trackTitle", songName);
                startActivity(intent);

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




