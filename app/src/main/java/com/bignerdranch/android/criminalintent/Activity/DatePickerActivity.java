package com.bignerdranch.android.criminalintent.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.Fragment.PickerFragment.DatePickerFragment;
import com.bignerdranch.android.criminalintent.Fragment.PickerFragment.PickerFragment;

import java.util.Date;
import java.util.UUID;

/**
 * Created by RBanks on 9/12/2016.
 */
public class DatePickerActivity extends SingleFragmentActivity {

    Date m_date;

    @Override
    protected Fragment createFragment() {
        Intent intent = getIntent();
        Date date = (Date) intent
                .getSerializableExtra(DatePickerFragment.EXTRA_DATE_TIME);

        DatePickerFragment fragment = DatePickerFragment.newInstance(date);

        return fragment;
    }

    public static Intent newIntent(Context context, Date date) {
        Intent intent = new Intent(context, DatePickerActivity.class);
        intent.putExtra(DatePickerFragment.EXTRA_DATE_TIME, date);
        return intent;
    }

    public void setDate(Date date) {
        m_date = date;
    }


}
