package com.example.teacher_panel_application.InsertData_Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener , DatePickerDialog.OnDateSetListener {

    private OnDateSetListener onDateSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker.
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);

        // Set the minimum date to the current date
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date the user picks.
        if (onDateSetListener != null) {
            onDateSetListener.onDateSet(year, month, day);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Not needed for this example
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        this.onDateSetListener = listener;
    }
}
