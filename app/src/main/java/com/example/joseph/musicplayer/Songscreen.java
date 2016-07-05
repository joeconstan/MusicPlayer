package com.example.joseph.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;



//this is the screen that appears when you click on a song in the listview. an individual song screen.
public class Songscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_screen);
        //Toolbar mToolbar = (Toolbar) findViewById(R.id.m_toolbar);
        //setSupportActionBar(mToolbar);


        Intent intent = getIntent();
        String songName = intent.getStringExtra("songTitle");


    }
}
