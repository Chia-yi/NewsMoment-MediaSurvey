package com.recoveryrecord.surveyandroid.example;

//package com.example.application.timepickerexample;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}