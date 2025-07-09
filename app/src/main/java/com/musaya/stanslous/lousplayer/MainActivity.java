package com.musaya.stanslous.lousplayer;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// ✅ Import your BottomSheet class
import com.musaya.stanslous.lousplayer.ui.BottomSheetMusic;

public class MainActivity extends AppCompatActivity {

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

        // ✅ Album art click opens bottom sheet
        ImageView albumArt = findViewById(R.id.albumArt);
        albumArt.setOnClickListener(v -> {
            BottomSheetMusic bottomSheet = new BottomSheetMusic();
            bottomSheet.show(getSupportFragmentManager(), "MusicList");
        });
    }
}
