package io.berrycorp.noti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import io.berrycorp.noti.adapters.NoteAdapter;
import io.berrycorp.noti.models.Note;

public class NoteActivity extends AppCompatActivity {


    private ListView lvNotes;
    private Button btnAdd;
    private NoteAdapter adapter;

    private ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        initialize();
        addControls();
        addEvents();
    }

    private void initialize() {
        notes = Note.all(this);
    }

    private void addControls() {
        lvNotes = findViewById(R.id.lv_notes);
        btnAdd = findViewById(R.id.btn_add);

        adapter = new NoteAdapter(this, R.layout.row_item_note, notes);
        lvNotes.setAdapter(adapter);
    }

    private void addEvents() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, AddNoteActivity.class);
                intent.putExtra("keyAction", "add");
                startActivity(intent);
            }
        });
    }
}
