package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.notemate.adapter.NoteAdapter;
import com.example.notemate.model.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteListener {

    public ImageButton imageView_add_note;
    public Database database;
    public NoteAdapter noteAdapter;
    public RecyclerView recyclerView;
    public ArrayList<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        imageView_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoteDetailsActivity.class);
                intent.putExtra("type", "newNote");
                startActivity(intent);
            }
        });
    }

    public void init() {
        database = new Database(this);
        noteList = new ArrayList<>();
        imageView_add_note = findViewById(R.id.imageView_add_note);
        recyclerView = findViewById(R.id.recyclerView_Notes);
        noteList.addAll(database.getMyNotes());
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);
    }

    public void updateNoteList() {
        noteList.clear();
        noteList.addAll(database.getMyNotes());
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNoteList();
    }

    @Override
    public void NoteClick(Note note) {
        Intent intent = new Intent(getApplicationContext(), NoteDetailsActivity.class);
        intent.putExtra("type", "updateNote");
        intent.putExtra("selectedNote", note);
        startActivity(intent);
    }
}