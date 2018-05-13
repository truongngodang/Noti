package io.berrycorp.noti.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper {

    public static final String DATABASE_NAME = "data.db";
    public static final String DB_PATH_SUFFIX = "/databases/";

    public static void initDatabaseFormAssets(Context context) {
        File databasePath = context.getDatabasePath(DATABASE_NAME);
        if (!databasePath.exists()){
            try {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME);

                File outDir = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);

                if (!outDir.exists()){
                    outDir.mkdir();
                }

                byte[] buffer = new byte[1024];

                int length;

                while((length = inputStream.read(buffer)) > 0){
                    outputStream.write(buffer, 0, length);
                }
                Toast.makeText(context,"Database Ready",Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
