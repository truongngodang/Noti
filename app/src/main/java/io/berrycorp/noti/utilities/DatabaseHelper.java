package io.berrycorp.noti.utilities;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper {

    public static final String DATABASE_NAME = "dataNoti.db";
    public static final String DB_PATH_SUFFIX = "/databases/";

    public static SQLiteDatabase initDatabaseFormAssets(Context context) {
        String databaseFileName = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
        File databaseFile = new File(databaseFileName);
        if (!databaseFile.exists()){
            try {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                File outDir = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);

                if (!outDir.exists()){
                    outDir.mkdir();
                }

                OutputStream outputStream = new FileOutputStream(databaseFileName);

                byte[] buffer = new byte[1024];

                int length;

                while((length = inputStream.read(buffer)) > 0){
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                inputStream.close();
                outputStream.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }
}
