package com.musaya.stanslous.lousplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.musaya.stanslous.lousplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicLoader {

    public static List<Song> loadMusic(Context context) {
        List<Song> songs = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA); // path

            do {
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String path = cursor.getString(dataColumn);
                songs.add(new Song(title, artist, path));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return songs;
    }
}
