package com.bignerdranch.android.criminalintent.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.CrimeLab;
import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by RBanks on 8/29/2016.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {
    private ViewPager m_viewPager;
    private List<Crime> m_crimeList;
    private static final String EXTRA_CRIME_ID = "Crime ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        m_viewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        m_crimeList = CrimeLab.getInstance(this).getCrimeList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        m_viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = m_crimeList.get(position);
                return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return m_crimeList.size();
            }
        });

        for (int i = 0; i < m_crimeList.size(); i++) {
            if(m_crimeList.get(i).getID().equals(crimeID)){
                m_viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context context, UUID id) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, id);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {

    }

    @Override
    public void onCrimeDeleted() {
        finish();
    }
}
