package com.example.notemate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notemate.model.Note;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "Notes";
    final static int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NOTE = "CREATE TABLE Notes (" + "ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "date VARCHAR , " +
                "title VARCHAR , " +
                "text VARCHAR , " +
                "web_url VARCHAR , " +
                "image_url VARCHAR , " +
                "color VARCHAR)";

        sqLiteDatabase.execSQL(CREATE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Note");
    }

    public void newNote(Note note) {
        SQLiteDatabase newSQLiteDatabase = this.getWritableDatabase();
        String INSERT_NOTE = "INSERT INTO Notes (date,title,text,web_url,image_url,color)" +
                "VALUES ('" + note.getDate() + "', '" + note.getTitle() + "', '" + note.getText() + "', '" + note.getWeb_url() + "', '" + note.getImage_url() + "', '" + note.getColor() + "')";

        newSQLiteDatabase.execSQL(INSERT_NOTE);
    }

    public void deleteNote(int ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String DELETE_NOTE = "DELETE FROM Notes WHERE ID=" + ID;
        sqLiteDatabase.execSQL(DELETE_NOTE);
        sqLiteDatabase.close();
    }

    public void updateNote(Note note) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String UPDATE_NOTE = "UPDATE Notes SET date='" + note.getDate()
                + "' , title='" + note.getTitle()
                + "' , text='" + note.getText()
                + "' , web_url='" + note.getWeb_url()
                + "' , image_url='" + note.getImage_url()
                + "' , color='" + note.getColor() + "' WHERE ID=" + note.getID();
        sqLiteDatabase.execSQL(UPDATE_NOTE);
        sqLiteDatabase.close();
    }

    public ArrayList<Note> getMyNotes() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String SELECT_NOTES = "SELECT * FROM Notes ORDER BY ID DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_NOTES, null);
        ArrayList<Note> getNotes = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            try {
                do {
                    Note note = new Note();
                    note.setID(cursor.getInt(0));
                    note.setDate(cursor.getString(1));
                    note.setTitle(cursor.getString(2));
                    note.setText(cursor.getString(3));
                    note.setWeb_url(cursor.getString(4));
                    note.setImage_url(cursor.getString(5));
                    note.setColor(cursor.getString(6));
                    getNotes.add(note);
                } while (cursor.moveToNext());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return getNotes;
    }
}