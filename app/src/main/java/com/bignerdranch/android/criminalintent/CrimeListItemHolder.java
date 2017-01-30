package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.Activity.CrimePagerActivity;
import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.Fragment.CrimeListFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RBanks on 8/29/2016.
 */
public class CrimeListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView m_titleTextView;
    public TextView m_dateTextView;
    public CheckBox m_solvedCheckBox;
    private Context m_context;
    private Crime m_crime;
    private CrimeListFragment.Callbacks m_callbacks;

    public CrimeListItemHolder(View view) {
        super(view);

        view.setOnClickListener(this);
        m_context = view.getContext();

        m_titleTextView = (TextView) view.findViewById(R.id.list_item_crime_title_text_view);
        m_dateTextView = (TextView) view.findViewById(R.id.list_item_crime_date_text_view);
        m_solvedCheckBox = (CheckBox) view.findViewById(R.id.list_item_crime_solved_check_box);
    }

    public void bindCrime(Crime crime) {
        m_crime = crime;
        Date date = crime.getDate();
        String dateString = CrimeFragment.m_dateFormatter.format(date);
        String timeString = CrimeFragment.m_timeFormatter.format(date);

        m_titleTextView.setText(crime.getTitle());
        m_dateTextView.setText(dateString + " at " + timeString);
        m_solvedCheckBox.setChecked(crime.isSolved());
    }


    @Override
    public void onClick(View v) {
//        Toast.makeText(itemView.getContext(),
//                m_crime.getTitle() + " clicked!",
//                Toast.LENGTH_SHORT).show();
//        Intent intent = CrimePagerActivity.newIntent(m_context, m_crime.getID());
//        m_context.startActivity(intent);
        m_callbacks.onCrimeSelected(m_crime);
    }

    public void setCallback(CrimeListFragment.Callbacks callback) {
        m_callbacks = callback;
    }
}
