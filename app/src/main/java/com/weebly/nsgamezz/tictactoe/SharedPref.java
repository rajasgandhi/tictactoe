package com.weebly.nsgamezz.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;

class SharedPref {

    private SharedPreferences mySharedPref;
    private SharedPreferences mySharedPref1;

    SharedPref(Context context) {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
        mySharedPref1 = context.getSharedPreferences("filename1", Context.MODE_PRIVATE);
    }
    // this method will save the nightMode State : True or False
    void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    void setSoundState(Boolean state1) {
        SharedPreferences.Editor editor1 = mySharedPref1.edit();
        editor1.putBoolean("SoundMode", state1);
        editor1.apply();
    }

    // this method will load the nightMode State
    Boolean loadNightModeState() {
        return mySharedPref.getBoolean("NightMode", false);
    }

    Boolean loadSoundState() {
        return mySharedPref1.getBoolean("SoundMode", true);
    }
}
