package com.bignerdranch.android.criminalintent.Fragment.PickerFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.bignerdranch.android.criminalintent.R;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by RBanks on 9/12/2016.
 * we create a date picker fragment; a dialog that will pop up when we select the date button
 */
public class DatePickerFragment extends PickerFragment {

    private DatePicker m_datePicker;
    private Button m_OK;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //TODO find out why we do this; it's not made clear to me in the book
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        getDate();

        m_datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        m_datePicker.init(getYear(), getMonth(), getDay(), null); //the last param being for seconds (I'll assume)

        //we only want this object to be visible on tablets
        m_OK = (Button) v.findViewById(R.id.ok_button_date);
        m_OK.setVisibility(View.GONE);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                //positive, neutral, and negative determine the location of the button in the dialog
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = m_datePicker.getYear();
                                int month = m_datePicker.getMonth();
                                int day = m_datePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        })
                //create returns the alert dialog instance
                .create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        getDate();

        m_datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        m_datePicker.init(getYear(), getMonth(), getDay(), null); //the last param being for seconds (I'll assume)

        m_OK = (Button) v.findViewById(R.id.ok_button_date);
        m_OK.setText(R.string.ok_button);
        m_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = m_datePicker.getYear();
                int month = m_datePicker.getMonth();
                int day = m_datePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
                sendResultToActivity(Activity.RESULT_OK, date);
                getActivity().finish();
            }
        });

        new AlertDialog.Builder(getActivity())
                .setView(v);
        return v;
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = getBundleArg(date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
