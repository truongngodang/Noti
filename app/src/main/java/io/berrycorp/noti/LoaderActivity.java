package io.berrycorp.noti;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.berrycorp.noti.utilities.DatabaseHelper;

import static io.berrycorp.noti.utilities.DatabaseHelper.DATABASE_NAME;

public class LoaderActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        LoadAsyncTask task = new LoadAsyncTask();
        task.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseHelper.initDatabaseFormAssets(LoaderActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(LoaderActivity.this, StartActivity.class);
            startActivity(intent);
        }
    }
}
