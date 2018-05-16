package io.berrycorp.noti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.berrycorp.noti.models.Note;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etName, etContent;
    private Button btnAdd, btnUpdate, btnCancel;

    private Intent intent = null;
    private Note noteToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        addControls();
        initialize();
        addEvents();
    }

    private void initialize() {

        intent = getIntent();

        if (intent.getStringExtra("keyAction").equals("update")) {
            btnAdd.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.VISIBLE);
            noteToUpdate = (Note) intent.getSerializableExtra("keyNote");
            etName.setText(noteToUpdate.getName());
            etContent.setText(noteToUpdate.getContent());
        } else if (intent.getStringExtra("keyAction").equals("add")) {

        }
    }

    private void addControls() {
        etName = findViewById(R.id.et_name);
        etContent = findViewById(R.id.et_content);
        btnAdd = findViewById(R.id.btn_add);
        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void addEvents() {

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
                String content = etContent.getText().toString();
                Note note = new Note(name, content);
                if (note.insert(AddNoteActivity.this) != -1) {
                    Intent intent = new Intent(AddNoteActivity.this, NoteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddNoteActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String content = etContent.getText().toString();
                Note note = new Note(name, content);
                note.setId(noteToUpdate.getId());
                if (note.update(AddNoteActivity.this)) {
                    Toast.makeText(AddNoteActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNoteActivity.this, NoteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddNoteActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
