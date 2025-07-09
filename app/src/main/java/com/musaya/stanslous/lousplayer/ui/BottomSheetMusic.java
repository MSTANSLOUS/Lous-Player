package com.musaya.stanslous.lousplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.musaya.stanslous.lousplayer.R;
import com.musaya.stanslous.lousplayer.adapter.SongAdapter;
import com.musaya.stanslous.lousplayer.model.Song;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomSheetMusic extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_music, container, false);

        RecyclerView musicRecyclerView = view.findViewById(R.id.musicRecyclerView);

        // 🔊 Dummy song list (you can later load real files)
        List<Song> songs = Arrays.asList(
                new Song("Love on the Brain", "Danti & Alaina"),
                new Song("God’s Plan", "Drake"),
                new Song("Ocean Eyes", "Billie Eilish"),
                new Song("One Dance", "Wiz kid & Drake"),
                new Song("My World", "Stanslous Musaya")
        );

        musicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        musicRecyclerView.setAdapter(new SongAdapter(songs));

        return view;
    }
}
