package com.example.notemate.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemate.NoteListener;
import com.example.notemate.R;
import com.example.notemate.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    ArrayList<Note> noteList;
    NoteListener listener;

    public NoteAdapter(ArrayList<Note> noteList, Activity activity) {
        this.noteList = noteList;
        listener = (NoteListener) activity;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.SetNote(noteList.get(position));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout linearLayout_container;
        public CardView cardView_container;
        public TextView textView_title;
        public TextView textView_note_text;
        public TextView textView_date;
        public TextView textView_url_note;
        public ImageView imageView_image_note;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout_container = itemView.findViewById(R.id.linearLayout_container);
            cardView_container = itemView.findViewById(R.id.cardView_container);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_note_text = itemView.findViewById(R.id.textView_note_text);
            textView_date = itemView.findViewById(R.id.textView_date);
            textView_url_note = itemView.findViewById(R.id.textView_url_note);
            imageView_image_note = itemView.findViewById(R.id.imageView_image_note);
        }

        public void SetNote(Note note) {
            textView_date.setText(note.getDate());
            textView_title.setText(note.getTitle());
            textView_note_text.setText(note.getText());

            cardView_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.NoteClick(note);
                }
            });

            if (!note.getImage_url().isEmpty() && note.getImage_url().length() > 4) {
                imageView_image_note.setVisibility(View.VISIBLE);
                imageView_image_note.setImageBitmap(BitmapFactory.decodeFile(note.getImage_url()));
            } else {
                imageView_image_note.setVisibility(View.GONE);
            }

            if (!note.getWeb_url().isEmpty() && note.getWeb_url().length() > 4) {
                textView_url_note.setVisibility(View.VISIBLE);
                textView_url_note.setText(note.getWeb_url());
            } else {
                textView_url_note.setVisibility(View.GONE);
            }

            linearLayout_container.setBackgroundColor(Color.parseColor(note.getColor()));
        }
    }
}