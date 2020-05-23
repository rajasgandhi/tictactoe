package com.weebly.nsgamezz.tictactoe;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.SettingsSlicesContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Set;


public class Settings extends AppCompatActivity {

    SharedPref sharedPref;
    SharedPref sharedPref1;

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";


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
        setContentView(R.layout.settings);

        Switch darkModeSwitch = findViewById(R.id.darkmode_switch);
        //Switch autoModeSwitch = findViewById(R.id.auto_dark_mode_switch);
        Switch soundSwitch = findViewById(R.id.sound_switch);
        Button backBtnSettings = findViewById(R.id.backBtnSettings);

        //Notification stuff
        Switch notificationSwitch = findViewById(R.id.notifSwitch);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String toastMessage;
            if(isChecked){

                long repeatInterval = AlarmManager.INTERVAL_DAY;
                long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

                if (alarmManager != null) {
                    alarmManager.setInexactRepeating
                            (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                    triggerTime, repeatInterval, notifyPendingIntent);
                }
                //Set the toast message for the "on" case.
                toastMessage = "Notifications Enabled!";
            } else {
                if (alarmManager != null) {
                    alarmManager.cancel(notifyPendingIntent);
                }
                mNotificationManager.cancelAll();
                //Set the toast message for the "off" case.
                toastMessage = "Notifications Disabled!";
            }

            //Show a toast to say the alarm is turned on or off.
            Toast.makeText(Settings.this, toastMessage, Toast.LENGTH_SHORT)
                    .show();

            createNotificationChannel();
        });

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d("on", "on");
                sharedPref.setNightModeState(true);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.d("off", "off");
                sharedPref.setNightModeState(false);
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        soundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                playSound();
                sharedPref1.setSoundState(true);
                 Intent intent = getIntent();
                 intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 finish();
                 startActivity(intent);
                 overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                stopSound();
                sharedPref1.setSoundState(false);
                 Intent intent = getIntent();
                 intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 finish();
                 startActivity(intent);
                 overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        backBtnSettings.setOnClickListener(
                view -> {
                    Intent intent = new Intent(Settings.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
        );
    }

    private void playSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Settings.this, R.raw.arockinthesun);
        if (!backgroundMusicMP.isPlaying()) {
            backgroundMusicMP.setVolume(40, 40);
            backgroundMusicMP.start();
            backgroundMusicMP.setLooping(true);
        }
    }

    private void stopSound() {
        final MediaPlayer backgroundMusicMP = MediaPlayer.create(Settings.this, R.raw.arockinthesun);
        backgroundMusicMP.stop();
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Tic Tac Toe notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Tells user to play TicTacToe");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("TicTacToe Alert")
                .setContentText("Get your daily dose of TicTacToe today right now!")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
