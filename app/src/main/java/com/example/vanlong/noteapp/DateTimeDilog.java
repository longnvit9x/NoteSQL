package com.example.vanlong.noteapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by vanlong on 4/21/2017.
 */

public class DateTimeDilog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private EditText edTime;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(getActivity().findViewById(R.id.edDate_Time)!=null)
        edTime= (EditText) getActivity().findViewById(R.id.edDate_Time);
        else  if(getActivity().findViewById(R.id.edDateTime_Detail)!=null)
            edTime= (EditText) getActivity().findViewById(R.id.edDateTime_Detail);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        String date = sdf.format(calendar.getTime());
        edTime.setText(date);
    }
}
