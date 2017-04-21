package com.example.vanlong.noteapp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.vanlong.controllers.FragmentController;
import com.example.vanlong.models.OnFragmentManager;

public class MainActivity extends AppCompatActivity implements OnFragmentManager {
    public Button btnNew, btnDelete, btnCancel;
    public TextView txtStatus;
    public static final FragmentListNote fragmentListNote = new FragmentListNote();
    public static FragmentNewNote fragmentNewNote = null;
    private FragmentController mFragmentController;

    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            txtStatus.setVisibility(View.VISIBLE);
            if (msg.getData().getString("Add", null)!=null)
                txtStatus.setText(msg.getData().getString("Add"));
            else if (msg.getData().getString("Delete", null)!=null) {
                txtStatus.setText(msg.getData().getString("Delete"));
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtStatus.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNew = (Button) findViewById(R.id.btnNew);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        addData();
        addEvent();
    }


    private void addEvent() {
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentNewNote = new FragmentNewNote();
                mFragmentController.replaceFragmentContent(R.id.fragment_container, fragmentNewNote,"NewNote","ListNote");
                isFragmenNewShow();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (fragmentNewNote==null) {
                   fragmentListNote.cancelDeleteData();

               }else{
                   onBackPressed();
                   fragmentNewNote=null;
                }
                isFragmenListNoteShow();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentListNote.deleteData()) {
                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("Delete", "Xóa thành công...");
                    msg.setData(bundle);
                    msg.sendToTarget();
                }

            }
        });


    }

    private void addData() {
        mFragmentController = new FragmentController(this);
        mFragmentController.replaceFragmentContent(R.id.fragment_container, fragmentListNote,"ListNote",null);
    }

    @Override
    public void onDataSelected(int data) {
        if (data == 3) {
            Log.e("theme", "");
            Message msg = mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putString("Add", "Thêm thành công...");
            msg.setData(bundle);
            msg.sendToTarget();
        }
    }

    public void isFragmenListNoteShow() {
        btnNew.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.INVISIBLE);
    }

    public void isFragmenNewShow() {
        btnDelete.setVisibility(View.INVISIBLE);
        btnCancel.setVisibility(View.VISIBLE);
        btnNew.setVisibility(View.INVISIBLE);
    }

}

