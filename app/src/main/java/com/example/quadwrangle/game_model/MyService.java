package com.example.quadwrangle.game_model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import com.example.quadwrangle.R;

public class MyService extends Service {

    static boolean working;

    //creating a media player object
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //getting systems default ringtone
        player = MediaPlayer.create(this, R.raw.music);
        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);

        //staring the player
        player.start();

        //we have some options for service
        //start sticky means service will be explicitly started and stopped
        return START_STICKY;
    }

    public static void setWorking() {
        working = !working;
    }

    public static boolean getWorking() {
        return working;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();
    }

}