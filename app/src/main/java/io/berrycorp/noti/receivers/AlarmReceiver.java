package io.berrycorp.noti.receivers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import io.berrycorp.noti.R;
import io.berrycorp.noti.ReminderActivity;
import io.berrycorp.noti.models.Task;
import io.berrycorp.noti.utilities.MusicSingleton;

import static io.berrycorp.noti.models.Task.BELL_SWEET_ALARM;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer player;
    private static final int TIME_VIBRATE = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getBundleExtra("DATA");
        Task task = (Task) extras.getSerializable("keyTask");

        Date current = Calendar.getInstance().getTime();

        if (current.after(task.getStartPoint())) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, task.getId(), new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        } else {

            Intent notificationIntent = new Intent(context, ReminderActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_bell_ring)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(task.getName())
                    .setAutoCancel(true)
                    .setPriority(6)
                    .setVibrate(new long[]{TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE, TIME_VIBRATE,
                            TIME_VIBRATE})
                    .setContentIntent(contentIntent);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(task.getId(), builder.build());

            final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            assert pm != null;
            final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "INFO");
            wakeLock.acquire(10 * 60 * 1000L);

            switch (task.getBell()) {
                case BELL_SWEET_ALARM: {
                    MusicSingleton.getInstance(context).playMusic(R.raw.sweet_alarm);
                    break;
                }
            }
        }
    }
}
