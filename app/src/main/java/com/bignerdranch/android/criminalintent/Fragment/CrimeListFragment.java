package com.bignerdranch.android.criminalintent.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.android.criminalintent.Activity.CrimePagerActivity;
import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.CrimeListItemHolder;
import com.bignerdranch.android.criminalintent.CrimeLab;
import com.bignerdranch.android.criminalintent.R;

import java.util.List;

/**
 * Created by RBanks on 8/29/2016.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView m_crimeRecyclerView;
    private CrimeAdapter m_adapter;
    private boolean m_subtitleVisible;
    private Button m_newCrimeButton;
    private View m_noCrimeView;
    private int m_crimeSize;
    private Callbacks m_callbacks;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        m_callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_callbacks = null;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, m_subtitleVisible);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout viewGroup = new LinearLayout(getContext());
        //add our view for if there isn't a crime
        m_noCrimeView = inflater.inflate(R.layout.no_crimes_fragment, container, false);
        m_newCrimeButton = (Button) m_noCrimeView.findViewById(R.id.no_crime_fragment_new_crime_button);
        m_newCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCrimeStartActivity();
            }
        });
        viewGroup.addView(m_noCrimeView);

        //add our view for if there is a crime
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        m_crimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        m_crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            m_subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        Log.i("info", "subtitle visible: " + m_subtitleVisible);
        viewGroup.addView(view);



        updateUI();

        return viewGroup;

    }

    public void updateUI(){
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        m_crimeSize = crimeLab.getCrimeList().size();
        List<Crime> crimes = crimeLab.getCrimeList();

        if(m_crimeSize > 0)
            m_noCrimeView.setVisibility(View.GONE);
        else
            m_noCrimeView.setVisibility(View.VISIBLE);

        if (m_adapter == null) {
            m_adapter = new CrimeAdapter(crimes);
            m_crimeRecyclerView.setAdapter(m_adapter);
        }
        else{
            int position = crimeLab.getSelected();
            //TODO create an attribute in crimeLab that flags if the item as been changed and then refresh the ones that have in order
            //m_adapter.notifyItemChanged(position);
            m_adapter.setCrimes(crimes);
            m_adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(m_subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                //add our crime item and start our activity
                addCrimeStartActivity();
                return true;
            case R.id.menu_item_show_subtitle:
                m_subtitleVisible = !m_subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addCrimeStartActivity() {
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
//        Intent intent = CrimePagerActivity
//                .newIntent(getActivity(), crime.getID());
//        startActivity(intent);
        m_callbacks.onCrimeSelected(crime);
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrimeList().size();
        //used to format the string and make sure the item inside the string object is substituted with the crime Count value
        //this function allows you to add as many objects as you need depending on how many variables are in the string
        //for plural strings, when using the quantity function, the second param is for the count, the other params are for substitution
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);
                          //getString(R.string.subtitle_format, crimeCount);

        if(!m_subtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        //shows the string we had on the bottom of the main title, kinda neat
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeListItemHolder> {
        private List<Crime> m_crimeList;

        public CrimeAdapter(List<Crime> crimes) {
            m_crimeList = crimes;
        }

        @Override
        public CrimeListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            CrimeListItemHolder holder = new CrimeListItemHolder(view);
            holder.setCallback(m_callbacks);
            return holder;
        }

        @Override
        public void onBindViewHolder(CrimeListItemHolder holder, int position) {
            Crime crime = m_crimeList.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return m_crimeList.size();
        }

        public void setCrimes(List<Crime> crimes) {
            m_crimeList = crimes;
        }
    }
}
