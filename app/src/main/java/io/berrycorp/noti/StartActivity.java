package io.berrycorp.noti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import io.berrycorp.noti.utilities.DatabaseHelper;

public class StartActivity extends AppCompatActivity {

    private LinearLayout layoutReminder, layoutNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Khoi tao database tu thu muc assets
        initialize();

        addControls();
        addEvents();

    }

    private void initialize() {
        DatabaseHelper.initDatabaseFormAssets(this);
    };

    private void addControls() {
        layoutReminder = findViewById(R.id.layout_reminder);
        layoutNote = findViewById(R.id.layout_note);
    }

    private void addEvents() {
        layoutReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ReminderActivity.class);
                startActivity(intent);
            }
        });
        layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
