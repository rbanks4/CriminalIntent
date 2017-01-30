package com.bignerdranch.android.criminalintent.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.Fragment.CrimeFragment;
import com.bignerdranch.android.criminalintent.Fragment.CrimeListFragment;
import com.bignerdranch.android.criminalintent.R;

/**
 * Created by RBanks on 8/29/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getID());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getID());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeleted() {
    }

    //    @Override
//    public boolean onPrepareOptionsMenu(final Menu menu) {
//        //for some reason it won't prepare to show on it's own so I have to find a way around it
//        getMenuInflater().inflate(R.menu.fragment_crime_list, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
}
