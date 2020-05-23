package com.weebly.nsgamezz.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Button;

public class About extends AppCompatActivity {

    SharedPref sharedPref;
    SharedPref sharedPref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        sharedPref1 = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        if (sharedPref1.loadSoundState()) {
            playSound();
        } else {
            stopSound();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Button backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(
                view -> {
                    Intent intent;
                    intent = new Intent(About.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
        );
    }

    private void playSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(About.this, R.raw.arockinthesun);
        if (!backgroundMusicMP.isPlaying()) {
            backgroundMusicMP.setVolume(40, 40);
            backgroundMusicMP.start();
            backgroundMusicMP.setLooping(true);
        }
    }

    private void stopSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(About.this, R.raw.arockinthesun);
        backgroundMusicMP.stop();
    }
}