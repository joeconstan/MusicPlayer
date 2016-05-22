package com.example.joseph.musicplayer;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{


    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




            if (Build.VERSION.SDK_INT >= 23) {
                boolean f = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (f)
                    Log.v(TAG,"Permission is granted");
                else {
                    Log.v(TAG, "Permission not granted");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            }



        ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{BaseColumns._ID, MediaStore.MediaColumns.TITLE, MediaStore.Audio.Media.DATA};
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Audio.Media.DATA, null, null);
        cursor.moveToFirst();
        int colIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
        String path = cursor.getString(colIndex); //this
        //Vector<String> songs = new Vector<>(0);
        Uri uri = Uri.parse(path);
        /*if (cursor.moveToFirst()) { //needs to check for first element to avoid nullptr exception
            do {
                songs.add(cursor.getString(1));
            } while (cursor.moveToNext());
        } */
        //boolean f = cursor.moveToFirst();
        //String x = String.valueOf(f);
        //songs.add(0, x);

        //----------------------------------
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(MainActivity.this, uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(Exception e){
            e.printStackTrace();
        }
        cursor.close();

        //----------------------------------

       /* ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songs);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToFirst();
                //Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(MainActivity.this, uri); //here
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }catch(Exception e){
                    e.printStackTrace();
                }
                cursor.close();
            }
        });


*/

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




