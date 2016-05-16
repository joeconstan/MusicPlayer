package com.example.joseph.musicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String items[] = {"song1", "song2", "song3", "song4","song5", "song6","song7", "song8", "song9", "song10"};
        //ListView listView = (ListView) findViewById(R.id.listView);
        //ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //listView.setAdapter(listAdapter);

        TextView text1 = (TextView)findViewById(R.id.text1);
        ArrayList SongList = new ArrayList<String>();
        ContentResolver resolver = getContentResolver(); //look up later
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; //look up later
        Cursor musicCursor = resolver.query(uri, null, null, null, null);
        String title;
        //if (musicCursor!=null && musicCursor.moveToFirst()){
            //int colindex = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            //do{
                //title = musicCursor.getString(colindex);

            //}
            //while (musicCursor.moveToNext());
        //}
        if (musicCursor!=null && musicCursor.moveToFirst()) {
            int colindex = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            title = musicCursor.getString(colindex);
            text1.setText(title);//doesnt work. getcolid too?
        }
    }
}




