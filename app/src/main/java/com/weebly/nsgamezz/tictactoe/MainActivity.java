package com.weebly.nsgamezz.tictactoe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    boolean singleSelected = true;

    SharedPref sharedPref;
    SharedPref sharedPref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        sharedPref1 = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES && (sharedPref.loadNightModeState())) {
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
        setContentView(R.layout.main_menu);

        Fabric.with(this, new Crashlytics());

        MobileAds.initialize(this,
                "ca-app-pub-9858435316265616~9201601163");

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final Button b1 = findViewById(R.id.b1);
        final Button b2 = findViewById(R.id.b2);
        final Button b3 = findViewById(R.id.b3);
        final Button play = findViewById(R.id.play);
        //final Button highScore = (Button) findViewById(R.id.highScores);
        //highScore.setVisibility(View.INVISIBLE);
        final RadioButton easy = findViewById(R.id.easyRBtn);
        easy.setChecked(true);
        final RadioButton difficult = findViewById(R.id.difficultRBtn);
        final RadioButton impossible = findViewById(R.id.impossibleRBtn);
        //final RadioButton singleDevice = findViewById(R.id.singleRBtn);
        //singleDevice.setChecked(true);
        //final RadioButton twoDevice = findViewById(R.id.doubleRBtn);

        final RadioGroup singlePlayer = findViewById(R.id.RGsinglePlayer);
        singlePlayer.setVisibility(View.VISIBLE);
        final RadioGroup twoPlayer = findViewById(R.id.RGtwoPlayer);
        twoPlayer.setVisibility(View.INVISIBLE);
        b2.setAlpha((float) 0.3);

        b1.setOnClickListener(
                view -> {
                    b1.setAlpha((float) 1);
                    b2.setAlpha((float) 0.3);
                    singleSelected = true;
                    singlePlayer.setVisibility(View.VISIBLE);
                    twoPlayer.setVisibility(View.INVISIBLE);
                    //highScore.setVisibility(View.INVISIBLE);
                }
        );

        b2.setOnClickListener(
                view -> {
                    b1.setAlpha((float) 0.3);
                    b2.setAlpha((float) 1);
                    singleSelected = false;
                    singlePlayer.setVisibility(View.INVISIBLE);
                    twoPlayer.setVisibility(View.VISIBLE);
                    //highScore.setVisibility(View.VISIBLE);
                }
        );

        b3.setOnClickListener(
                view -> {
                    Intent intent;
                    intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
        );

        play.setOnClickListener(
                v -> {
                    if (singleSelected) {
                        if (easy.isChecked()) {
                            Intent intent;
                            intent = new Intent(MainActivity.this, Player1Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else if (difficult.isChecked()) {
                            Intent intent;
                            intent = new Intent(MainActivity.this, Player1ActivityDifficult.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else if (impossible.isChecked()) {
                            Intent intent;
                            intent = new Intent(MainActivity.this, Player1ActivityImpossible.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }
                    } else {
                        /*if (singleDevice.isChecked()) {
                            //twoDevice.setChecked(false);
                            singleDevice.setChecked(true);
                            Intent intent;
                            intent = new Intent(MainActivity.this, Player2Activity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        } else if (twoDevice.isChecked()){
                            singleDevice.setChecked(false);
                            //twoDevice.setChecked(true);
                            Intent intent;
                            intent = new Intent(MainActivity.this, Player2Activity2Devices.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }*/

                        Intent intent = new Intent(MainActivity.this, Player2Activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        if(!(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        } else {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();
                    if (menuItem.getItemId() == R.id.settings) {
                        Intent intent = new Intent(MainActivity.this, Settings.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                            /*case R.id.profile:
                            Intent intent1 = new Intent(MainActivity.this, Profile.class);
                            startActivity(intent1);
                            break;*/
                    }
                    return true;
                });

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(MainActivity.this, R.raw.arockinthesun);
        if (!backgroundMusicMP.isPlaying()) {
            backgroundMusicMP.setVolume(40, 40);
            backgroundMusicMP.start();
            backgroundMusicMP.setLooping(true);
        }
    }

    private void stopSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(MainActivity.this, R.raw.arockinthesun);
        backgroundMusicMP.stop();
    }
}
