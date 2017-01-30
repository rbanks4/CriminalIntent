package com.bignerdranch.android.criminalintent.Fragment.PickerFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.Tools;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by RBanks on 9/12/2016.
 */
public class TimePickerFragment extends PickerFragment {

    private TimePicker m_timePicker;

    private Button m_OK;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //TODO find out why we do this; it's not made clear to me in the book
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        getDate();

        setupTimepicker(v);

        m_OK = (Button) v.findViewById(R.id.ok_button_time);
        m_OK.setVisibility(View.GONE);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                        //positive, neutral, and negative determine the location of the button in the dialog
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour;
                                int minute;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    hour = m_timePicker.getHour();
                                    minute = m_timePicker.getMinute();
                                }
                                 else {
                                    hour = m_timePicker.getCurrentHour();
                                    minute = m_timePicker.getCurrentMinute();
                                }
                                Date date = new GregorianCalendar(getYear(), getMonth(), getDay(), hour, minute).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                        //create returns the alert dialog instance
                .create();
    }

    private void setupTimepicker(View v) {
        m_timePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        //we use the deprecated version since the newer "getHour" requires a higher api version
        m_timePicker.setCurrentHour(getHour());
        m_timePicker.setCurrentMinute(getMinute());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        getDate();

        setupTimepicker(v);

        m_OK = (Button) v.findViewById(R.id.ok_button_time);
        m_OK.setText(R.string.ok_button);
        m_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = m_timePicker.getCurrentHour();
                int minute = m_timePicker.getCurrentMinute();
                Date date = new GregorianCalendar(getYear(), getMonth(), getDay(), hour, minute).getTime();
                sendResultToActivity(Activity.RESULT_OK, date);
                getActivity().finish();
            }
        });

        new AlertDialog.Builder(getActivity())
                .setView(v);
        return v;
    }

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = getBundleArg(date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
