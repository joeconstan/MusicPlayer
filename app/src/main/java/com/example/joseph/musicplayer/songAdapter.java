package com.example.joseph.musicplayer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class songAdapter extends ArrayAdapter<Song>{
    public songAdapter(Context context, Song[] songs){
        super(context, 0, songs);
    }


    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_row, parent, false);
        Song song = getItem(position);
        TextView titleText = (TextView) customView.findViewById(R.id.titleText);
        TextView artistText = (TextView) customView.findViewById(R.id.artistText);
        titleText.setText(song.getTitle());
        artistText.setText(song.getArtist());
        return customView;

    }


}
