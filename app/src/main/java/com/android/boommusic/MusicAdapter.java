package com.android.boommusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends  RecyclerView.Adapter<MusicAdapter.ViewHolder>  {

  ArrayList<AudioModel> songList;
  Context context;

    public MusicAdapter(ArrayList<AudioModel> songList, Context context) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.music_recyclwerviewitem,parent,false);
       return  new MusicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AudioModel songData = songList.get(position);
        holder.songTxt.setText(songData.getTitle());

        if (MyMediaPlayer.currentIndex == position) {
            // Set color for the selected item
            holder.songTxt.setTextColor(Color.parseColor("#ff0000")); // Change to the desired color
        } else {
            // Set color for items other than the selected one
            holder.songTxt.setTextColor(Color.parseColor("#ffffff")); // Change to the desired color
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activity to another activity
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent = new Intent(context, Playmusic.class);
                intent.putExtra("LIST", songList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }


        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public  class  ViewHolder extends  RecyclerView.ViewHolder{

    ImageView musicIConImageView;
    TextView songTxt;
    public ViewHolder(View itemView) {
        super(itemView);
        musicIConImageView = itemView.findViewById(R.id.musicDisc);
        songTxt = itemView.findViewById(R.id.musicTxt);

    }
}

}
