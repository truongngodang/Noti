package io.berrycorp.noti.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import io.berrycorp.noti.R;
import io.berrycorp.noti.ReminderActivity;

public class AlarmService extends Service {

    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent dialogIntent = new Intent(this, ReminderActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
        player = MediaPlayer.create(this, R.raw.sweet_alarm);
        player.setLooping(true);
        player.start();
        return START_STICKY;
    }
}
