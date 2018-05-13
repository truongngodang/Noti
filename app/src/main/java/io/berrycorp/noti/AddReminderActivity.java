package io.berrycorp.noti;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.berrycorp.noti.models.Task;
import io.berrycorp.noti.receivers.AlarmReceiver;

public class AddReminderActivity extends AppCompatActivity {

    private Spinner spBell;
    private Spinner spRemindType;

    private TextView etName, tvStartDate, tvStartTime, etContent, tvRemindDate, tvRemindTime;
    private Button btnAdd, btnCancel, btnUpdate;

    private ArrayList<String> bells = new ArrayList<>();
    private ArrayList<String> remindTypes = new ArrayList<>();
    private Intent intent = null;
    private Task taskToUpdate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private SimpleDateFormat onlyDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat onlyTimeFormat = new SimpleDateFormat("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        addControls();

        initialize();

        addEvents();
    }

    private void initialize() {
        // Adapter spinner
        bells.add("Sweet Alarm");
        remindTypes.add("1 Giờ");
        remindTypes.add("1 Ngày");
        remindTypes.add("1 Tuần");
        remindTypes.add("1 Tháng");
        remindTypes.add("1 Năm");
        ArrayAdapter<String> bellAdapter = new ArrayAdapter<String >(AddReminderActivity.this, R.layout.support_simple_spinner_dropdown_item, bells);
        spBell.setAdapter(bellAdapter);

        ArrayAdapter<String> remindTypeAdapter = new ArrayAdapter<String >(AddReminderActivity.this, R.layout.support_simple_spinner_dropdown_item, remindTypes);
        spRemindType.setAdapter(remindTypeAdapter);

        intent = getIntent();
        if (intent.getStringExtra("keyAction").equals("update")) {
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);

            taskToUpdate = (Task) intent.getSerializableExtra("keyTask");
            etName.setText(taskToUpdate.getName());
            etContent.setText(taskToUpdate.getDesc());
            tvStartDate.setText(onlyDateFormat.format(taskToUpdate.getStartPoint()));
            tvStartTime.setText(onlyTimeFormat.format(taskToUpdate.getStartPoint()));
            tvRemindDate.setText(onlyDateFormat.format(taskToUpdate.getRemindPoint()));
            tvRemindTime.setText(onlyTimeFormat.format(taskToUpdate.getRemindPoint()));
            spBell.setSelection(taskToUpdate.getBell());
            spRemindType.setSelection(taskToUpdate.getRemindType());
        } else if (intent.getStringExtra("keyAction").equals("add")) {
            Calendar current = Calendar.getInstance();
            tvStartDate.setText(onlyDateFormat.format(current.getTime()));
            tvStartTime.setText(onlyTimeFormat.format(current.getTime()));
            tvRemindDate.setText(onlyDateFormat.format(current.getTime()));
            tvRemindTime.setText(onlyTimeFormat.format(current.getTime()));
        }
    }

    private void addControls() {
        spBell = findViewById(R.id.sp_bell);
        spRemindType = findViewById(R.id.sp_remind_type);
        etName = findViewById(R.id.et_name);
        tvStartDate = findViewById(R.id.tv_date_start);
        tvStartTime = findViewById(R.id.tv_time_start);
        etContent = findViewById(R.id.et_content);
        tvRemindDate = findViewById(R.id.tv_date_remind);
        tvRemindTime = findViewById(R.id.tv_time_remind);
        btnAdd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btn_cancel);
        btnUpdate = findViewById(R.id.btn_update);
    }

    private void addEvents() {

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar timePick = Calendar.getInstance();
                try {
                    timePick.setTime(onlyDateFormat.parse(tvStartDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        timePick.set(year, monthOfYear, dayOfMonth);
                        tvStartDate.setText(onlyDateFormat.format(timePick.getTime()));
                    }

                }, timePick.get(Calendar.YEAR), timePick.get(Calendar.MONTH), timePick.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar timePick = Calendar.getInstance();
                try {
                    timePick.setTime(onlyTimeFormat.parse(tvStartTime.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timePick.set(Calendar.HOUR_OF_DAY, hour);
                        timePick.set(Calendar.MINUTE, minute);
                        tvStartTime.setText(onlyTimeFormat.format(timePick.getTime()));
                    }
                }, timePick.get(Calendar.HOUR_OF_DAY), timePick.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        tvRemindDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar timePick = Calendar.getInstance();
                try {
                    timePick.setTime(onlyDateFormat.parse(tvRemindDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        timePick.set(year, monthOfYear, dayOfMonth);
                        tvRemindDate.setText(onlyDateFormat.format(timePick.getTime()));
                    }

                }, timePick.get(Calendar.YEAR), timePick.get(Calendar.MONTH), timePick.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tvRemindTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar timePick = Calendar.getInstance();
                try {
                    timePick.setTime(onlyTimeFormat.parse(tvRemindTime.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timePick.set(Calendar.HOUR_OF_DAY, hour);
                        timePick.set(Calendar.MINUTE, minute);
                        tvRemindTime.setText(onlyTimeFormat.format(timePick.getTime()));
                    }
                }, timePick.get(Calendar.HOUR_OF_DAY), timePick.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                Date startPoint = null;
                Date remindPoint = null;
                try {
                    startPoint = dateFormat.parse(tvStartDate.getText().toString() + " " + tvStartTime.getText().toString());
                    remindPoint = dateFormat.parse(tvRemindDate.getText().toString() + " " + tvRemindTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String desc = etContent.getText().toString();
                int bell = spBell.getSelectedItemPosition();
                int remindType = spRemindType.getSelectedItemPosition();
                Task task = new Task(name, startPoint, desc, remindPoint, bell, remindType);
                int res = task.insert(AddReminderActivity.this);
                if (res != 0 && res != -1) {
                    Toast.makeText(AddReminderActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    task.setId(res);
                    setAlarm(task);
                    Intent intent = new Intent(AddReminderActivity.this, ReminderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddReminderActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date startPoint = null;
                Date remindPoint = null;
                try {
                    startPoint = dateFormat.parse(tvStartDate.getText().toString() + " " + tvStartTime.getText().toString());
                    remindPoint = dateFormat.parse(tvRemindDate.getText().toString() + " " + tvRemindTime.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String name = etName.getText().toString();
                String desc = etContent.getText().toString();
                int bell = spBell.getSelectedItemPosition();
                int remindType = spRemindType.getSelectedItemPosition();
                int id = taskToUpdate.getId();
                Task task = new Task(name, startPoint, desc, remindPoint, bell, remindType);
                task.setId(id);
                if (task.update(AddReminderActivity.this)) {
                    Toast.makeText(AddReminderActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    setAlarm(task);
                    Intent intent = new Intent(AddReminderActivity.this, ReminderActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddReminderActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAlarm(Task task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AddReminderActivity.this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyTask", task);
        intent.putExtra("DATA", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminderActivity.this, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.getRemindPoint());
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60*1000, pendingIntent);
    }
}
