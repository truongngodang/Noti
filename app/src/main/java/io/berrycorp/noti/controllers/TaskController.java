package io.berrycorp.noti.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

import io.berrycorp.noti.ReminderActivity;
import io.berrycorp.noti.models.Task;
import io.berrycorp.noti.receivers.AlarmReceiver;

import static io.berrycorp.noti.models.Task.AFTER_A_MINUTE;
import static io.berrycorp.noti.models.Task.AFTER_DAY;
import static io.berrycorp.noti.models.Task.AFTER_HOUR;
import static io.berrycorp.noti.models.Task.AFTER_TEN_MINUTE;
import static io.berrycorp.noti.models.Task.AFTER_WEEK;

public class TaskController {

    private Task mTask;
    private Context mContext;

    public TaskController(Task mTask, Context mContext) {
        this.mTask = mTask;
        this.mContext = mContext;
    }


    public void createTask() {
        int idTask = mTask.insert(mContext);
        if ( idTask != -1) {
            Toast.makeText(mContext, "Thêm thành công", Toast.LENGTH_SHORT).show();
            mTask.setId(idTask);
            setAlarmReceiver();
            Intent intent = new Intent(mContext, ReminderActivity.class);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "Thêm thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateTask() {
        if (mTask.update(mContext)) {
            Toast.makeText(mContext, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            setAlarmReceiver();
            Intent intent = new Intent(mContext, ReminderActivity.class);
            mContext.startActivity(intent);
        } else {
            Toast.makeText(mContext, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteTask() {
        if (mTask.delete(mContext)) {
            removeAlarmReceiver();
            return true;
        } else {
            return false;
        }
    }

    private void setAlarmReceiver() {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyTask", mTask);
        intent.putExtra("DATA", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, mTask.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mTask.getRemindPoint());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getRemindLoop(), pendingIntent);
    }

    private void removeAlarmReceiver() {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, mTask.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
    }

    private Integer getRemindLoop() {
        switch (mTask.getRemindType()) {
            case AFTER_A_MINUTE : {
                return 60*1000;
            }
            case AFTER_TEN_MINUTE : {
                return 60*1000*10;
            }
            case AFTER_HOUR : {
                return 60*1000*60;
            }
            case AFTER_DAY : {
                return 60*1000*60*24;
            }
            case AFTER_WEEK : {
                return 60*1000*60*24*7;
            }
        }
        return 0;
    }
}
