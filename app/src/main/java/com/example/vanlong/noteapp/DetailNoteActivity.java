package com.example.vanlong.noteapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanlong.controllers.NoteController;
import com.example.vanlong.models.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by vanlong on 4/13/2017.
 */

public class DetailNoteActivity extends AppCompatActivity {
    private EditText edContent_Detail ,edTitle_Detail, edDateTime_Detail;;
    private Button btnDelete_Detail, btnUpdate_Detail;
    private boolean reslf = false;
    private NoteController mNoteController;
    Note note = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        edTitle_Detail = (EditText) findViewById(R.id.edTitle_Detail);
        edDateTime_Detail = (EditText) findViewById(R.id.edDateTime_Detail);
        edContent_Detail = (EditText) findViewById(R.id.edContent_Detail);
        btnDelete_Detail = (Button) findViewById(R.id.btnDelete_Detail);
        btnUpdate_Detail = (Button) findViewById(R.id.btnUpdate_Detail);
        getDataD();
        addEvent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent intent = new Intent(DetailNoteActivity.this, MainActivity.class);
       startActivity(intent);
       finish();
    }

    private void getDataD() {
        mNoteController = new NoteController(this);
        note = (Note) getIntent().getBundleExtra("Data").getSerializable("Note");
        edContent_Detail.setText(note.getmContent());
        edDateTime_Detail.setText(note.getmDate());
        edTitle_Detail.setText(note.getmTitle());

    }

    private void addEvent() {

        edDateTime_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeDilog date = new DateTimeDilog();
                date.show(getFragmentManager(), "Ngày");
            }
        });
        btnUpdate_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note noteupdate = new Note();
                noteupdate.setmId(note.getmId());
                noteupdate.setmTitle(edTitle_Detail.getText().toString());
                noteupdate.setmContent(edContent_Detail.getText().toString());
                noteupdate.setmDate(edDateTime_Detail.getText().toString());
                try {
                    if (mNoteController.updateData(noteupdate)) {
                        Toast.makeText(DetailNoteActivity.this, "Sửa thành công..!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.e("Erorr: ", ex.toString());
                }
//                Intent intent = new Intent(DetailNoteActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
        btnDelete_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DetailNoteActivity.this);
                builder.setMessage("Bạn có thực sự muốn xóa hay không?")
                        .setCancelable(false)
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mNoteController.deleteData(note);
                                Intent intent = new Intent(DetailNoteActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        });
                builder.create().show();
            }

        });
    }


}
