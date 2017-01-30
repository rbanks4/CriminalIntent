package com.bignerdranch.android.criminalintent.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragment.PickerFragment.DatePickerFragment;
import com.bignerdranch.android.criminalintent.Fragment.PickerFragment.TimePickerFragment;

import java.util.Date;

/**
 * Created by RBanks on 9/13/2016.
 */
public class TimePickerActivity extends SingleFragmentActivity {

    Date m_date;

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        Date date = (Date) intent
                .getSerializableExtra(TimePickerFragment.EXTRA_DATE_TIME);

        TimePickerFragment fragment = TimePickerFragment.newInstance(date);

        return fragment;
    }

    public static Intent newIntent(Context context, Date date) {
        Intent intent = new Intent(context, TimePickerActivity.class);
        intent.putExtra(TimePickerFragment.EXTRA_DATE_TIME, date);
        return intent;
    }

    public void setDate(Date date) {
        m_date = date;
    }

}
