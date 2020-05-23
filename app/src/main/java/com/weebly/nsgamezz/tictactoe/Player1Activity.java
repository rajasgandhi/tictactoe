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

public class Player1Activity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int computerPoints;

    private TextView textViewPlayer1;
    private TextView computer;

    SharedPref sharedPref;
    SharedPref sharedPref1;
    SharedPref sharedPref2;

    private InterstitialAd mInterstitialAd;

    private Random waitTimeRandom = new Random();
    private int waitTimeRandomInt = waitTimeRandom.nextInt(3);
    private int waitTime = waitTimeRandomInt;

    private int waitTimeReset = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        sharedPref1 = new SharedPref(this);
        sharedPref2 = new SharedPref(this);

        if (sharedPref.loadNightModeState()) {
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
        setContentView(R.layout.activity_player1);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        computer = findViewById(R.id.computer);

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

        Button backBtnPlayer1 = findViewById(R.id.backBtnPlayer1);

        MobileAds.initialize(this,
                "ca-app-pub-9858435316265616~9201601163");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9858435316265616/1400974449");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //mInterstitialAd.show();

        Random random = new Random();
        int random1 = random.nextInt(3);

        backBtnPlayer1.setOnClickListener(v -> {
            //if (random1 == 1) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent intent = new Intent(Player1Activity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                } else {
                    Intent intent = new Intent(Player1Activity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            /*} else {
                Intent intent = new Intent(Player1Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }*/

        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                //finish();
                Intent intent = new Intent(Player1Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    @Override
    public void onClick(View v) {

        final MediaPlayer popSoundMP = MediaPlayer.create(Player1Activity.this, R.raw.popsound);

        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if(sharedPref2.loadSoundState()) {
            popSoundMP.start();
        } else {
            popSoundMP.stop();
        }

        ((Button) v).setText("X");
        //new Handler().postDelayed(this::computerMove, waitTime);
        computerMove();
        roundCount = roundCount + 2;

        if(checkForWin()) {
            if(player1Turn) {
                //new Handler().postDelayed(this::player1Wins, waitTimeReset);
                player1Wins();
            } else {
                //new Handler().postDelayed(this::player2Wins, waitTimeReset);
                player2Wins();
            }
        } else if (roundCount >= 9) {
            //new Handler().postDelayed(this::draw, waitTimeReset);
            draw();
        }

        player1Turn = true;
    }

    /*private boolean computerMove() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        Random x = new Random();
        int y = x.nextInt(9);

        if (y == 0 && field[0][0].equals("")) {

            buttons[0][0].setText("O");
            return true;
        } else if (y == 1 && field[0][1].equals("")) {

            buttons[0][1].setText("O");
            return true;
        } else if (y == 2 && field[0][2].equals("")) {

            buttons[0][2].setText("O");
            return true;
        } else if (y == 3 && field[1][0].equals("")) {

            buttons[1][0].setText("O");
            return true;
        } else if (y == 4 && field[1][1].equals("")) {

            buttons[1][1].setText("O");
            return true;
        } else if (y == 5 && field[1][2].equals("")) {

            buttons[1][2].setText("O");
            return true;
        } else if (y == 6 && field[2][0].equals("")) {

            buttons[2][0].setText("O");
            return true;
        } else if (y == 7 && field[2][1].equals("")) {

            buttons[2][1].setText("O");
            return true;
        } else if (y == 8 && field[2][2].equals("")) {

            buttons[2][2].setText("O");
            return true;
        } else {
            for (int i = 0; i < 3; i++) {
                if (field[i][0].equals("")) {

                    buttons[i][0].setText("O");
                    return true;
                } else if (field[i][1].equals("")) {

                    buttons[i][1].setText("O");
                    return true;
                } else if (field[i][2].equals("")) {

                    buttons[i][2].setText("O");
                    return true;
                }
            }
        }
        return false;
    }*/

    private boolean checkForCompWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }


        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals("O") &&
                    field[i][1].equals("O") &&
                    field[i][2].equals("")) {

                buttons[i][2].setText("O");
                return true;
            } else if (field[i][0].equals("O") &&
                    field[i][2].equals("O") &&
                    field[i][1].equals("")) {

                buttons[i][1].setText("O");
                return true;
            } else if (field[i][1].equals("O") &&
                    field[i][2].equals("O") &&
                    field[i][0].equals("")) {

                buttons[i][2].setText("O");
                return true;
            } else if (field[0][i].equals("O") &&
                    field[1][i].equals("O") &&
                    field[2][i].equals("")) {

                buttons[2][i].setText("O");
                return true;
            } else if (field[0][i].equals("O") &&
                    field[2][i].equals("O") &&
                    field[1][i].equals("")) {

                buttons[1][i].setText("O");
                return true;
            } else if (field[1][i].equals("O") &&
                    field[2][i].equals("O") &&
                    field[0][i].equals("")) {

                buttons[0][i].setText("O");
                return true;
            }
        }
        return false;
    }

    private boolean checkForHumWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }


        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals("X") &&
                    field[i][1].equals("X") &&
                    field[i][2].equals("")) {

                buttons[i][2].setText("O");
                return true;
            } else if (field[i][0].equals("X") &&
                    field[i][2].equals("X") &&
                    field[i][1].equals("")) {

                buttons[i][1].setText("O");
                return true;
            } else if (field[i][1].equals("X") &&
                    field[i][2].equals("X") &&
                    field[i][0].equals("")) {

                buttons[i][0].setText("O");
                return true;
            } else if (field[0][i].equals("X") &&
                    field[1][i].equals("X") &&
                    field[2][i].equals("")) {
                buttons[2][i].setText("O");
                return true;
            } else if (field[0][i].equals("X") &&
                    field[2][i].equals("X") &&
                    field[1][i].equals("")) {

                buttons[1][i].setText("O");
                return true;
            } else if (field[1][i].equals("X") &&
                    field[2][i].equals("X") &&
                    field[0][i].equals("")) {

                buttons[0][i].setText("O");
                return true;
            }
        }
        return false;
    }

    private boolean checkForCenter() {

        if (buttons[1][1].getText().toString().equals("")) {
            buttons[1][1].setText("O");
            return true;
        }
        return false;
    }

    private boolean checkForDiag() {

        if (buttons[0][0].getText().toString().equals("X") && buttons[2][2].getText().toString().equals("")) {

            buttons[2][2].setText("O");
            return true;
        } else if (buttons[0][2].getText().toString().equals("X") && buttons[2][0].getText().toString().equals("")) {

            buttons[2][0].setText("O");
            return true;
        } else if (buttons[2][0].getText().toString().equals("X") && buttons[0][2].getText().toString().equals("")) {

            buttons[0][2].setText("O");
            return true;
        } else if (buttons[2][2].getText().toString().equals("X") && buttons[0][0].getText().toString().equals("")) {

            buttons[0][0].setText("O");
            return true;
        } else if (buttons[0][0].getText().toString().equals("O") && buttons[2][2].getText().toString().equals("")) {

            buttons[2][2].setText("O");
            return true;
        } else if (buttons[0][2].getText().toString().equals("O") && buttons[2][0].getText().toString().equals("")) {

            buttons[2][0].setText("O");
            return true;
        } else if (buttons[2][0].getText().toString().equals("O") && buttons[0][2].getText().toString().equals("")) {

            buttons[0][2].setText("O");
            return true;
        } else if (buttons[2][2].getText().toString().equals("O") && buttons[0][0].getText().toString().equals("")) {

            buttons[0][0].setText("O");
            return true;
        }
        return false;
    }

    private boolean makeRandom() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }


        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals("")) {

                buttons[i][0].setText("O");
                return true;
            } else if (field[i][1].equals("")) {

                buttons[i][1].setText("O");
                return true;
            } else if (field[i][2].equals("")) {

                buttons[i][2].setText("O");
                return true;
            }
        }
        return false;
    }

    private boolean computerMove() {
        Random r1 = new Random();
        int chance = r1.nextInt(101);
        if(chance > 70) {
            if (checkForCompWin()) {
                return true;
            } else {
                if (checkForHumWin()) {
                    return true;
                } else {
                    if (checkForCenter()) {
                        return true;
                    } else {
                        if (checkForDiag()) {
                            return true;
                        } else {
                            makeRandom();
                        }
                    }
                }
            }
        } else {
            makeRandom();
        }
        return false;
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
        player1Points++;
        Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        computerPoints++;
        Toast.makeText(this, "The phone wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player: " + player1Points);
        computer.setText("Phone: " + computerPoints);
    }

    private void resetGame() {
        player1Points = 0;
        computerPoints = 0;
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
        roundCount = 0;
        player1Turn = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", computerPoints);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        computerPoints = savedInstanceState.getInt("computerPoints");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    private void playSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Player1Activity.this, R.raw.arockinthesun);
        if(!backgroundMusicMP.isPlaying()) {
            backgroundMusicMP.setVolume(20,20);
            backgroundMusicMP.start();
            backgroundMusicMP.setLooping(true);
        }
    }

    private void stopSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Player1Activity.this, R.raw.arockinthesun);
        backgroundMusicMP.stop();

    }
}