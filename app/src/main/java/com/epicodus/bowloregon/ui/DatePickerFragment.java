package com.epicodus.bowloregon.ui;

/**
 * Created by Daren on 6/20/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public interface DatePickDialogListener {
        void onFinishEditDialog(int year, int month, int day);
    }

    public static DatePickerFragment newInstance(Context context) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePicker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        DatePickDialogListener listener = (DatePickDialogListener) getActivity();
        listener.onFinishEditDialog(year, month, day);
    }

}