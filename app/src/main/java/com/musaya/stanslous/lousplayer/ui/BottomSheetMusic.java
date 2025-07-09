package com.musaya.stanslous.lousplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.musaya.stanslous.lousplayer.R;
import com.musaya.stanslous.lousplayer.adapter.SongAdapter;
import com.musaya.stanslous.lousplayer.model.Song;
import com.musaya.stanslous.lousplayer.utils.MusicLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomSheetMusic extends BottomSheetDialogFragment {

    // Listener interface for song clicks
    public interface SongClickListener {
        void onSongClicked(String path);
    }

    private SongClickListener listener;

    // Use this method to create new instance and pass listener
    public static BottomSheetMusic newInstance(SongClickListener listener) {
        BottomSheetMusic fragment = new BottomSheetMusic();
        fragment.setSongClickListener(listener);
        return fragment;
    }

    public void setSongClickListener(SongClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_music, container, false);

        RecyclerView musicRecyclerView = view.findViewById(R.id.musicRecyclerView);

        List<Song> songs = MusicLoader.loadMusic(requireContext());

        musicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        musicRecyclerView.setAdapter(new SongAdapter(songs, path -> {
            if (listener != null) {
                listener.onSongClicked(path);
            }
            dismiss(); // close the bottom sheet on click
        }));

        return view;
    }
}
