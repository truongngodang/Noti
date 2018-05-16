package io.berrycorp.noti.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import static io.berrycorp.noti.utilities.DatabaseHelper.initDatabaseFormAssets;

public class Note implements Serializable {
    private int id;
    private String name;
    private String content;

    public Note() {
    }

    public Note(String name, String content) {
        this.name = name;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static ArrayList<Note> all(Context context) {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase database = initDatabaseFormAssets(context);
        Cursor cursor = database.rawQuery("SELECT * FROM NOTES",null);
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Note note = new Note(name, content);
                note.setId(Integer.valueOf(id));
                notes.add(note);
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.close();
        return notes;
    }

    public int insert(Context context) {
        SQLiteDatabase database = initDatabaseFormAssets(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", this.name);
        contentValues.put("content", this.content);
        int res = (int) database.insert("NOTES", null, contentValues);
        database.close();
        return res;
    }

    public boolean update(Context context) {
        SQLiteDatabase database = initDatabaseFormAssets(context);
        String query = "UPDATE NOTES SET name='"+ this.name +"', content='"+ content +"' WHERE id='"+ this.id +"'";
        database.execSQL(query);
        database.close();
        return  true;
    }

    public boolean delete(Context context) {
        SQLiteDatabase database = initDatabaseFormAssets(context);
        String query = "DELETE FROM NOTES WHERE id='" + this.id + "'";
        database.execSQL(query);
        database.close();
        return true;
    }
}
