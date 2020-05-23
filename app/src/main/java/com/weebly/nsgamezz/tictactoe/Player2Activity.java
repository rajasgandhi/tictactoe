package com.weebly.nsgamezz.tictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


import java.util.Random;

public class Player2Activity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    SharedPref sharedPref;
    SharedPref sharedPref1;
    SharedPref sharedPref2;

    private InterstitialAd mInterstitialAd;

    private int waitTimeReset = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        sharedPref1 = new SharedPref(this);
        sharedPref2 = new SharedPref(this);

         if (sharedPref.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        if(sharedPref1.loadSoundState()) {
            playSound();
        } else {
            stopSound();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player2);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset1 = findViewById(R.id.button_reset);
        buttonReset1.setOnClickListener(v -> resetBoard());

        Button buttonReset2 = findViewById(R.id.button_reset_all);
        buttonReset2.setOnClickListener(v -> resetGame());

        Button backBtnPlayer2 = findViewById(R.id.backBtnPlayer2);

        MobileAds.initialize(this,
                "ca-app-pub-9858435316265616~9201601163");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9858435316265616/1400974449");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.show();

        Random random = new Random();
        int random1 = random.nextInt(3);

        backBtnPlayer2.setOnClickListener(v -> {
            //if (random1 == 1) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent intent = new Intent(Player2Activity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    Intent intent = new Intent(Player2Activity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            //}
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                //finish();
                Intent intent = new Intent(Player2Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final MediaPlayer popSoundMP = MediaPlayer.create(Player2Activity.this, R.raw.popsound);
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            if(sharedPref2.loadSoundState()) {
                popSoundMP.start();
            } else {
                popSoundMP.stop();
            }
            ((Button) v).setText("X");
        } else {
            if(sharedPref2.loadSoundState()) {
                popSoundMP.start();
            } else {
                popSoundMP.stop();
            }
            popSoundMP.start();
            ((Button) v).setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                new Handler().postDelayed(this::player1Wins, waitTimeReset);
                roundCount = 0;
            } else {
                new Handler().postDelayed(this::player2Wins, waitTimeReset);
                roundCount = 0;
            }
        } else if (roundCount == 9) {
            new Handler().postDelayed(this::draw, waitTimeReset);
        } else {
            player1Turn = !player1Turn;
        }

    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void player1Wins() {
        roundCount = 0;
        player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        roundCount = 0;
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
        roundCount = 0;
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }


    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
        roundCount = 0;
        player1Turn = true;
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        player1Turn = true;
        roundCount = 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    private void playSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Player2Activity.this, R.raw.arockinthesun);
        if(!backgroundMusicMP.isPlaying()) {
            backgroundMusicMP.setVolume(20,20);
            backgroundMusicMP.start();
            backgroundMusicMP.setLooping(true);
        }
    }

    private void stopSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Player2Activity.this, R.raw.arockinthesun);
        backgroundMusicMP.stop();
    }
}
