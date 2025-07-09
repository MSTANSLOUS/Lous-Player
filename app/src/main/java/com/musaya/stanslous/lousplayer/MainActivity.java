package com.musaya.stanslous.lousplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.musaya.stanslous.lousplayer.model.Song;
import com.musaya.stanslous.lousplayer.ui.BottomSheetMusic;
import com.musaya.stanslous.lousplayer.utils.MusicLoader;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomSheetMusic.SongClickListener {

    private MediaPlayer mediaPlayer;
    private List<Song> songList;
    private int currentSongIndex = -1;

    private TextView songTitle, artistName, currentTime, totalTime;
    private ImageButton btnPlay, btnNext, btnPrevious;
    private ImageView albumArt;
    private SeekBar seekBar;

    private Handler handler = new Handler();
    private Runnable updateSeekBarRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        songTitle = findViewById(R.id.songTitle);
        artistName = findViewById(R.id.artistName);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        albumArt = findViewById(R.id.albumArt);
        seekBar = findViewById(R.id.seekBar);

        // Load songs now to have list ready
        songList = MusicLoader.loadMusic(this);

        // Set default UI texts
        if (!songList.isEmpty()) {
            updateUIForSong(songList.get(0));
        }

        findViewById(R.id.albumArt).setOnClickListener(v -> {
            BottomSheetMusic bottomSheet = BottomSheetMusic.newInstance(this);
            bottomSheet.show(getSupportFragmentManager(), "MusicList");
        });

        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                if (currentSongIndex == -1 && !songList.isEmpty()) {
                    // Start from first song if none selected yet
                    playSong(0);
                } else {
                    resumeMusic();
                }
            }
        });

        btnNext.setOnClickListener(v -> playNextSong());

        btnPrevious.setOnClickListener(v -> playPreviousSong());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean userTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    updateCurrentTimeText();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userTouch = false;
            }
        });

        requestMusicPermission();
    }

    private void updateUIForSong(Song song) {
        songTitle.setText(song.getTitle());
        artistName.setText(song.getArtist());
        // TODO: Set albumArt image if available (for now it stays static)
    }

    private void playSong(int index) {
        if (index < 0 || index >= songList.size()) return;

        currentSongIndex = index;
        Song song = songList.get(index);
        updateUIForSong(song);

        if (mediaPlayer != null) {
            mediaPlayer.reset();
        } else {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.ic_pause);  // Switch play icon to pause
            setupSeekBar();

            mediaPlayer.setOnCompletionListener(mp -> playNextSong());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing song: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.ic_play);
            handler.removeCallbacks(updateSeekBarRunnable);
        }
    }

    private void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.ic_pause);
            updateSeekBar();
        }
    }

    private void playNextSong() {
        if (songList.isEmpty()) return;
        int nextIndex = (currentSongIndex + 1) % songList.size();
        playSong(nextIndex);
    }

    private void playPreviousSong() {
        if (songList.isEmpty()) return;
        int prevIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
        playSong(prevIndex);
    }

    private void setupSeekBar() {
        if (mediaPlayer == null) return;

        seekBar.setMax(mediaPlayer.getDuration());
        totalTime.setText(formatMilliseconds(mediaPlayer.getDuration()));
        updateSeekBar();
    }

    private void updateSeekBar() {
        if (mediaPlayer == null) return;

        updateSeekBarRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    updateCurrentTimeText();
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(updateSeekBarRunnable);
    }

    private void updateCurrentTimeText() {
        if (mediaPlayer != null) {
            currentTime.setText(formatMilliseconds(mediaPlayer.getCurrentPosition()));
        }
    }

    private String formatMilliseconds(int ms) {
        int seconds = ms / 1000;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    private void requestMusicPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1001);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
            }
        }
    }

    @Override
    public void onSongClicked(String path) {
        // Find the song index from path
        for (int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getPath().equals(path)) {
                playSong(i);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacks(updateSeekBarRunnable);
            mediaPlayer.release();
        }
    }
}
