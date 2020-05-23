package com.weebly.nsgamezz.tictactoe;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

public class Profile extends AppCompatActivity {

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Profile.this, R.raw.arockinthesun);
        if(!backgroundMusicMP.isPlaying()) {
            backgroundMusicMP.setVolume(40,40);
            backgroundMusicMP.start();
            backgroundMusicMP.setLooping(true);
        }

        sharedPref = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
    }
}
