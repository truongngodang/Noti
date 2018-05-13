package io.berrycorp.noti.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import io.berrycorp.noti.R;
import io.berrycorp.noti.ReminderActivity;
import io.berrycorp.noti.models.Task;
import io.berrycorp.noti.services.AlarmService;

import static io.berrycorp.noti.models.Task.BELL_SWEET_ALARM;

public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer player;
    @Override
    public void onReceive(Context context, Intent intent) {

        final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.ON_AFTER_RELEASE, "INFO");
        wakeLock.acquire(10*60*1000L);

        Bundle extras = intent.getBundleExtra("DATA");
        Task task = (Task) extras.getSerializable("keyTask");

        Intent dialogIntent = new Intent(context, ReminderActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialogIntent);

        switch (task.getBell()) {
            case BELL_SWEET_ALARM : {
                player = MediaPlayer.create(context, R.raw.sweet_alarm);
                player.start();
                break;
            }
        }

        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                player.stop();
                player.release();
                wakeLock.release();
            }
        });
    }
}
