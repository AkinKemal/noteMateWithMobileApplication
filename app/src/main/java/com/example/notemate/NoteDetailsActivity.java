package com.example.notemate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.notemate.model.Note;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailsActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 99;
    private static final int SELECT_IMAGE = 100;
    private String color = "#9E9E9E";
    public EditText edittext_title_note;
    public EditText edittext_note;
    public EditText editText_input_url;
    public TextView textView_date;
    public TextView textView_note_url;
    public ImageView imageView_image_note;
    public ImageButton imageView_done_icon;
    public ImageButton imageView_arrow_back_icon;
    public ImageButton imageView_add_image;
    public ImageButton imageView_delete_image;
    public ImageButton imageView_add_url;
    public ImageButton imageView_delete_url;
    public ImageButton imageView_delete_note;
    public RadioGroup color_radio_group;
    public RadioButton radioButton_color1;
    public RadioButton radioButton_color2;
    public RadioButton radioButton_color3;
    public RadioButton radioButton_color4;
    public RadioButton radioButton_color5;
    public RadioButton radioButton_color6;
    public LinearLayout linearLayout_url;
    public ConstraintLayout constraintLayout_image_note_container;
    public Database database;
    public Note selectedNote;
    public String type = null;
    public String imageURL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        type = getIntent().getStringExtra("type");
        database = new Database(this);
        edittext_title_note = findViewById(R.id.edittext_title_note);
        edittext_note = findViewById(R.id.edittext_note);
        textView_date = findViewById(R.id.textView_date);
        textView_note_url = findViewById(R.id.textView_note_url);
        imageView_delete_note = findViewById(R.id.imageView_delete_note);
        imageView_image_note = findViewById(R.id.imageView_image_note);
        imageView_done_icon = findViewById(R.id.imageView_done_icon);
        imageView_arrow_back_icon = findViewById(R.id.imageView_arrow_back_icon);
        imageView_add_image = findViewById(R.id.imageView_add_image);
        imageView_delete_image = findViewById(R.id.imageView_delete_image);
        imageView_add_url = findViewById(R.id.imageView_add_url);
        imageView_delete_url = findViewById(R.id.imageView_delete_url);
        color_radio_group = findViewById(R.id.color_radio_group);
        radioButton_color1 = findViewById(R.id.radioButton_color1);
        radioButton_color2 = findViewById(R.id.radioButton_color2);
        radioButton_color3 = findViewById(R.id.radioButton_color3);
        radioButton_color4 = findViewById(R.id.radioButton_color4);
        radioButton_color5 = findViewById(R.id.radioButton_color5);
        radioButton_color6 = findViewById(R.id.radioButton_color6);
        linearLayout_url = findViewById(R.id.linearLayout_url);
        constraintLayout_image_note_container = findViewById(R.id.constraintLayout_image_note_container);

        if (type.equals("updateNote")) {
            selectedNote = (Note) getIntent().getSerializableExtra("selectedNote");
            edittext_title_note.setText(selectedNote.getTitle());
            edittext_note.setText(selectedNote.getText());
            textView_date.setText(selectedNote.getDate());
            if (selectedNote.getImage_url().length() > 4 && selectedNote.getImage_url() != null) {
                constraintLayout_image_note_container.setVisibility(View.VISIBLE);
                imageView_image_note.setImageBitmap(BitmapFactory.decodeFile(selectedNote.getImage_url()));
            }
            if (selectedNote.getWeb_url().length() > 4 && selectedNote.getWeb_url() != null) {
                linearLayout_url.setVisibility(View.VISIBLE);
                textView_note_url.setText(selectedNote.getWeb_url());
            }
            setColor(selectedNote.getColor());
            checkColorButton(getColor());
        } else {
            imageView_delete_note.setVisibility(View.GONE);
            textView_date.setText(new SimpleDateFormat("EEEE , dd MMMM yyyy HH:mm", Locale.getDefault()).format(new Date()));
        }

        imageView_done_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edittext_title_note.getText().toString().trim().isEmpty()) {
                    edittext_title_note.setError("Title cannot be left blank");
                } else if (edittext_note.getText().toString().trim().isEmpty()) {
                    edittext_note.setError("Please enter note");
                } else {
                    if (type.equals("updateNote")) {
                        database.updateNote(new Note(selectedNote.getID(), textView_date.getText().toString(), edittext_title_note.getText().toString(), edittext_note.getText().toString(), linearLayout_url.getVisibility() == View.VISIBLE ? textView_note_url.getText().toString() : null, constraintLayout_image_note_container.getVisibility() == View.VISIBLE ? selectedNote.getImage_url() : null, getColor()));
                    } else {
                        database.newNote(new Note(textView_date.getText().toString(), edittext_title_note.getText().toString(), edittext_note.getText().toString(), linearLayout_url.getVisibility() == View.VISIBLE ? textView_note_url.getText().toString() : null, imageURL, getColor()));
                    }
                    finish();
                }
            }
        });

        imageView_arrow_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        imageView_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                constraintLayout_image_note_container.setVisibility(View.GONE);
                imageURL = null;
                imageView_image_note.setImageBitmap(null);
            }
        });

        imageView_add_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWebURL();
            }
        });

        imageView_delete_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_note_url.setText("");
                linearLayout_url.setVisibility(View.GONE);
            }
        });

        imageView_delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        color_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int colorResId;
                switch (checkedId) {
                    case R.id.radioButton_color1:
                        colorResId = R.color.note_color_1;
                        break;
                    case R.id.radioButton_color2:
                        colorResId = R.color.note_color_2;
                        break;
                    case R.id.radioButton_color3:
                        colorResId = R.color.note_color_3;
                        break;
                    case R.id.radioButton_color4:
                        colorResId = R.color.note_color_4;
                        break;
                    case R.id.radioButton_color5:
                        colorResId = R.color.note_color_5;
                        break;
                    case R.id.radioButton_color6:
                        colorResId = R.color.note_color_6;
                        break;
                    default:
                        colorResId = R.color.note_color_7;
                        break;
                }
                int color = getResources().getColor(colorResId);
                setColor(String.format("#%06X", (0xFFFFFF & color)));
            }
        });

    }

    private void showDeleteConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        Button textButton_confirm = dialog.findViewById(R.id.textButton_confirm);
        Button textButton_cancel = dialog.findViewById(R.id.textButton_cancel);

        textButton_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.deleteNote(selectedNote.getID());
                dialog.dismiss();
                finish();
            }
        });

        textButton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, PERMISSION_CODE);
        } else {
            pickImage();
        }
    }

    private void addWebURL() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_url);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

        Button textButton_add = dialog.findViewById(R.id.textButton_add);
        Button textButton_cancel = dialog.findViewById(R.id.textButton_cancel);
        editText_input_url = dialog.findViewById(R.id.editText_input_url);

        textButton_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editText_input_url.getText().toString().isEmpty() && Patterns.WEB_URL.matcher(editText_input_url.getText().toString()).matches()) {
                    textView_note_url.setText(editText_input_url.getText().toString());
                    linearLayout_url.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                } else {
                    editText_input_url.setError("Please enter a valid URL");
                }
            }
        });

        textButton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    private String getPath(Uri uri) {
        String filePath = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                filePath = cursor.getString(index);
            }
            cursor.close();
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView_image_note.setImageBitmap(bitmap);
                    constraintLayout_image_note_container.setVisibility(View.VISIBLE);
                    imageURL = getPath(uri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void checkColorButton(String color) {

        switch (color) {
            case "#03A9F4":
                radioButton_color1.setChecked(true);
                break;
            case "#8BC34A":
                radioButton_color2.setChecked(true);
                break;
            case "#F06292":
                radioButton_color3.setChecked(true);
                break;
            case "#FF7043":
                radioButton_color4.setChecked(true);
                break;
            case "#BA68C8":
                radioButton_color5.setChecked(true);
                break;
            case "#9E9E9E":
                radioButton_color6.setChecked(true);
                break;
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}