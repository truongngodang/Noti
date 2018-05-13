package io.berrycorp.noti;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.berrycorp.noti.adapters.TaskAdapter;
import io.berrycorp.noti.models.Task;


public class ReminderActivity extends AppCompatActivity {

    private Button btnAdd;
    private ListView lvTasks;
    private TaskAdapter adapter;

    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initialize();
        addControls();
        addEvents();
    }

    private void initialize() {
        try {
            tasks = Task.all(this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addControls() {
        btnAdd = findViewById(R.id.btn_add);
        lvTasks = findViewById(R.id.lv_tasks);

        adapter = new TaskAdapter(this, R.layout.row_item_task, tasks);
        lvTasks.setAdapter(adapter);
    }

    private void addEvents() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
                intent.putExtra("keyAction", "add");
                startActivity(intent);
            }
        });
    }
}
