package com.example.vanlong.noteapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.vanlong.controllers.FragmentController;
import com.example.vanlong.models.Note;
import com.example.vanlong.models.OnFragmentManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vanlong on 4/13/2017.
 */

public class FragmentNewNote extends Fragment {
    EditText edTitle_New, edContent_New,edDate_Time;
        Button btnAdd_New;
    private FragmentController mFragmentController;
    private OnFragmentManager mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        edTitle_New = (EditText) view.findViewById(R.id.edTitile_New);
        edContent_New = (EditText) view.findViewById(R.id.edContent_New);
        edDate_Time= (EditText) view.findViewById(R.id.edDate_Time);
        btnAdd_New = (Button) view.findViewById(R.id.btnAdd_New);
        addControl();
        addEvent();
        return view;
    }


    private void addEvent() {
        btnAdd_New.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                Note note = new Note(edTitle_New.getText().toString(), edContent_New.getText().toString(), edDate_Time.getText().toString());
                reloadData(MainActivity.fragmentListNote, note);
                getActivity().findViewById(R.id.btnNew).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.btnDelete).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.btnCancel).setVisibility(View.INVISIBLE);
                getActivity().onBackPressed();
            }
        });
        edDate_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeDilog date = new DateTimeDilog();
                date.show(getFragmentManager(), "Ngày");
            }
        });
    }

    private void addControl() {
        mFragmentController = new FragmentController(getActivity());
    }

    public void reloadData(Fragment fragment, Note note) {
        if (fragment instanceof FragmentListNote) {
           if( ((FragmentListNote) fragment).reloadData(note)){
               mListener.onDataSelected(3);
           };
        }
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentManager){
            mListener= (OnFragmentManager ) context;
        } else {
           Log.e("Warning", "must implement onViewSelected");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentManager){
            mListener= (OnFragmentManager ) activity;
        } else {
            Log.e("Warning", "must implement onViewSelected");
        }
    }

}
