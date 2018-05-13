package io.berrycorp.noti.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.berrycorp.noti.receivers.AlarmReceiver;

import static android.content.Context.MODE_PRIVATE;
import static io.berrycorp.noti.utilities.DatabaseHelper.DATABASE_NAME;

public class Task implements Serializable {
    private int id;
    private String name;
    private Date startPoint;
    private String desc;
    private Date remindPoint;
    private int bell;
    private int remindType;

    public static final int AFTER_HOUR = 0;
    public static final int AFTER_DAY = 1;
    public static final int AFTER_WEEK = 1;
    public static final int AFTER_MONTH = 2;
    public static final int AFTER_YEAR = 3;

    public static final int BELL_SWEET_ALARM = 0;

    public static final int REMINDER_REMINDING = 0;
    public static final int REMINDER_IDLE = 1;
    public static final int REMINDER_FINISHED = 2;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public Task(String name, Date startPoint, String desc, Date remindPoint, int bell, int remindType) {
        this.name = name;
        this.startPoint = startPoint;
        this.desc = desc;
        this.remindPoint = remindPoint;
        this.bell = bell;
        this.remindType = remindType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Date startPoint) {
        this.startPoint = startPoint;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getRemindPoint() {
        return remindPoint;
    }

    public void setRemindPoint(Date remindPoint) {
        this.remindPoint = remindPoint;
    }

    public int getBell() {
        return bell;
    }

    public void setBell(int bell) {
        this.bell = bell;
    }

    public int getRemindType() {
        return remindType;
    }

    public void setRemindType(int remindType) {
        this.remindType = remindType;
    }

    public int getState() {
        Date current = Calendar.getInstance().getTime();
        if (current.after(this.startPoint)) {
            return REMINDER_FINISHED;
        } else if (current.after(this.remindPoint)) {
            return REMINDER_REMINDING;
        } else {
            return REMINDER_IDLE;
        }
    }

    public int insert(Context context) {
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String startPoint = DATE_FORMAT.format(this.startPoint);
        String remindPoint = DATE_FORMAT.format(this.remindPoint);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", this.name);
        contentValues.put("start_point", startPoint);
        contentValues.put("desc", this.desc);
        contentValues.put("remind_point", remindPoint);
        contentValues.put("bell", this.bell);
        contentValues.put("remind_type", this.remindType);
        if (!this.startPoint.after(this.remindPoint)) {
            return  0;
        } else  {
            return (int) database.insert("TASKS", null, contentValues);
        }
    }

    public boolean update(Context context) {
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String startPoint = DATE_FORMAT.format(this.startPoint);
        String remindPoint = DATE_FORMAT.format(this.remindPoint);
        String query = "UPDATE TASKS SET name='"+ this.name +"', start_point='"+ startPoint +"', " +
                "desc='"+ this.desc +"', remind_point='"+ remindPoint +"', bell='" + this.bell +"', remind_type='" + this.remindType + "' WHERE id='"+ this.id +"'";
        if (!this.startPoint.after(this.remindPoint)) {
            return  false;
        } else  {
            database.execSQL(query);
            return  true;
        }
    }

    public boolean delete(Context context) {
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        String query = "DELETE FROM TASKS WHERE id='" + this.id + "'";
        database.execSQL(query);
        return true;
    }

    public static ArrayList<Task> all(Context context) throws ParseException {
        ArrayList<Task> tasks = new ArrayList<>();
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("SELECT * FROM TASKS",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String desc = cursor.getString(cursor.getColumnIndex("desc"));
                Date startPoint =  DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex("start_point")));
                Date remindPoint =  DATE_FORMAT.parse(cursor.getString(cursor.getColumnIndex("remind_point")));
                int bell =  cursor.getInt(cursor.getColumnIndex("bell"));
                int remindType =  cursor.getInt(cursor.getColumnIndex("remind_type"));
                Task task = new Task(name, startPoint, desc, remindPoint, bell, remindType);
                task.setId(Integer.valueOf(id));
                tasks.add(task);
                cursor.moveToNext();
            }
        }
        cursor.close();
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return  String.valueOf(t1.getState()).compareTo(String.valueOf(t2.getState()));
            }
        });
        return tasks;
    }
}
