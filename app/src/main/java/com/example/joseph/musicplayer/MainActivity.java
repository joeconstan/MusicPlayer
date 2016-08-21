

package com.example.joseph.musicplayer;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    final MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
    boolean playing = false;
    boolean prepared = false;

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
        final Song songs[] = new Song[21];
        for (int i = 0; i < 21; i++)
            songs[i] = new Song();
        queueSongs(cursor, songs);
        cursor.close();

        ListAdapter listAdapter = new songAdapter(this, songs);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);

        final TextView titlePanelText = (TextView) findViewById(R.id.titlePanelText);





        final ImageButton playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (playing) {
                        Log.v(TAG, "about to stop");

                        playing = false;

                    } else {
                        int id = getResources().getIdentifier("pause_butt_white", "mipmap", getPackageName());
                        playButton.setImageResource(id);
                        Intent i = new Intent(MainActivity.this, MusicService.class);
                        MainActivity.this.startService(i);
                        playing = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


// int id = getResources().getIdentifier("play_butt_white", "mipmap", getPackageName());
        //playButton.setImageResource(id);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (playing) {
                       //stop
                    }

                    for (int i=0;i<21;i++){
                        songs[i].setPlaying(false);
                    }
                    int isf = getResources().getIdentifier("pause_butt_white", "mipmap", getPackageName());
                    playButton.setImageResource(isf);
                    songs[position].setPlaying(true);
                    Uri urii = songs[position].getUri();
                   //change data source
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        RelativeLayout buttonPanel = (RelativeLayout) findViewById(R.id.buttonPanel);
        buttonPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), Songscreen.class);
                for (int i=0;i<21;i++){ //find out which song is playing
                    if (songs[i].getPlaying())
                        intent1.putExtra("songTitle", songs[i].getTitle());
                        intent1.putExtra("uri", songs[i].getUri().toString());
                }
                startActivity(intent1);
            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }

}



