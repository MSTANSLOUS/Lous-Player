package com.musaya.stanslous.lousplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musaya.stanslous.lousplayer.R;
import com.musaya.stanslous.lousplayer.model.Song;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    public interface SongClickListener {
        void onSongClicked(String path);
    }

    private final List<Song> songs;
    private final SongClickListener listener;

    public SongAdapter(List<Song> songs, SongClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSongClicked(song.getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;

        public SongViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.songTitle);
            artist = v.findViewById(R.id.artistName);
        }
    }
}
