package com.musaya.stanslous.lousplayer.model;

public class Song {
    private final String title;
    private final String artist;
    private final String path;

    public Song(String title, String artist, String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getPath() { return path; }
}
