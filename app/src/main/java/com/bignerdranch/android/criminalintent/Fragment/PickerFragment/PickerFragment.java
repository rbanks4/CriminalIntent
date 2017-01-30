package com.bignerdranch.android.criminalintent.Fragment.PickerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by RBanks on 9/12/2016.
 */
public class PickerFragment extends DialogFragment {

    private int m_year;
    private int m_month;
    private int m_day;
    private int m_hour;
    private int m_minute;

    public static final String EXTRA_DATE_TIME = "extra date time";

    public static final String ARG_DATE_TIME = "date Fragment";

    private static final String DATE_TIME_PICKER_LOG = "DateTimePicker";

    /**
     * Initialize our dates
     * @return
     */
    public Date getDate() {
        Date date = (Date) getArguments().getSerializable(ARG_DATE_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        m_year = calendar.get(Calendar.YEAR);
        m_month = calendar.get(Calendar.MONTH);
        m_day = calendar.get(Calendar.DAY_OF_MONTH);
        m_hour = calendar.get(Calendar.HOUR);
        m_minute = calendar.get(Calendar.MINUTE);

        return date;
    }

    public int getYear() {
        return m_year;
    }

    public void setYear(int year) {
        m_year = year;
    }

    public int getMonth() {
        return m_month;
    }

    public void setMonth(int month) {
        m_month = month;
    }

    public int getDay() {
        return m_day;
    }

    public void setDay(int day) {
        m_day = day;
    }

    public int getHour() {
        return m_hour;
    }

    public void setHour(int hour) {
        m_hour = hour;
    }

    public int getMinute() {
        return m_minute;
    }
    public void setMinute(int minute) {
        m_minute = minute;
    }

    public void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            Log.w(DATE_TIME_PICKER_LOG, "Target Fragment was null");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_TIME, date);

        Log.i(DATE_TIME_PICKER_LOG, "About to send result date: "
                        + getTargetRequestCode()
                        + " " + resultCode
                        + " " + date
        );
        //we grab the fragment that called this one and manually call 'onActivityResult' when the ok button is pressed
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public void sendResultToActivity(int resultCode, Date date){

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_TIME, date);

        getActivity().setResult(resultCode, intent);
    }

    public static Bundle getBundleArg(Date date) {
        Bundle args = new Bundle();
        //we create a bundle to save our date
        args.putSerializable(ARG_DATE_TIME, date);

        return args;
    }
}
