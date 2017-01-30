package com.bignerdranch.android.criminalintent.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.bignerdranch.android.criminalintent.Activity.DatePickerActivity;
import com.bignerdranch.android.criminalintent.Activity.TimePickerActivity;
import com.bignerdranch.android.criminalintent.Crime;
import com.bignerdranch.android.criminalintent.CrimeLab;
import com.bignerdranch.android.criminalintent.Fragment.PickerFragment.DatePickerFragment;
import com.bignerdranch.android.criminalintent.Fragment.PickerFragment.TimePickerFragment;
import com.bignerdranch.android.criminalintent.PictureUtils;
import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.Tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by RBanks on 8/22/2016.
 */
public class CrimeFragment extends Fragment {
    private Crime m_crime;
    private EditText m_titleField;
    private Button m_dateButton;
    private Button m_timeButton;
    private Button m_suspectButton;
    private Button m_reportButton;
    private ImageButton m_photoButton;
    private ImageView m_photoView;
    private File m_photoFile;

    private CheckBox m_solvedCheckBox;
    private Context m_context;
    private static final String ARG_CRIME_ID = "crime_id_arg";

    private static final String CRIME_LOG = "Crime Fragment Logs";
    private static final String DIALOG_DATE =  "Dialog Date";
    private static final String DIALOG_TIME =  "Dialog Time";

    public static final SimpleDateFormat m_dateFormatter = new SimpleDateFormat("EEEE, MMM dd, yyyy");
    public static final SimpleDateFormat m_timeFormatter = new SimpleDateFormat("hh:mm aa");

    public static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    private Callbacks m_callbacks;

    /**
     * Required interface for hosting activites
     */
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
        void onCrimeDeleted();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        m_callbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_callbacks = null;
    }

    private void updateCrime() {
        CrimeLab.getInstance(getActivity()).updateCrime(m_crime);
        m_callbacks.onCrimeUpdated(m_crime);
    }

    public static Fragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, id);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(CRIME_LOG, "onCreate -- CrimeFragment");
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        m_crime = new Crime();
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        m_crime = CrimeLab.getInstance(getActivity()).getCrime(crimeID);
        m_photoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(m_crime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //we inflate our fragment by passing in the layout resource ID, the parent container, and weather to add the inflated view to the view parent
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        m_context = getContext();

        m_titleField = (EditText) v.findViewById(R.id.crime_title);
        m_titleField.setText(m_crime.getTitle());
        m_titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //this is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //we only want to change the title if the text has been changed
                m_crime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //this one too
            }
        });



        m_dateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        //this will make the button greyed out -- we comment this out to use our alert dialog widget
        //m_dateButton.setEnabled(false);
        m_dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we construct our date picker and pass in a fragment manager and tag to show it (any tag'll do)
                //DatePickerFragment datePicker = new DatePickerFragment();
                try {

                    if(Tools.isTablet(getContext())) {
                        //TODO make sure the fragment can use onActivityResult
                        Intent intent = DatePickerActivity.newIntent(getContext(), m_crime.getDate());
                        startActivityForResult(intent, REQUEST_DATE);
                    }
                    else {
                        DatePickerFragment datePicker = DatePickerFragment.newInstance(m_crime.getDate());
                        FragmentManager manager = getFragmentManager();
                        datePicker.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                        datePicker.show(manager, DIALOG_DATE);
                    }
                }
                catch(Exception e){
                    Log.e(CRIME_LOG, "Error trying to set the date", e);
                }
            }
        });

        //I kinda like that name..."crime_time" heh-heh
        m_timeButton = (Button) v.findViewById(R.id.crime_time);
        updateTime();
        m_timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Tools.isTablet(getContext())) {
                    Intent intent = TimePickerActivity.newIntent(getContext(), m_crime.getDate());
                    startActivityForResult(intent, REQUEST_TIME);
                }
                else {
                    TimePickerFragment timePicker = TimePickerFragment.newInstance(m_crime.getDate());
                    FragmentManager manager = getFragmentManager();
                    timePicker.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                    timePicker.show(manager, DIALOG_TIME);
                }
            }
        });

        m_solvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        m_solvedCheckBox.setChecked(m_crime.isSolved());
        m_solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set the crime's solved property
                m_crime.setSolved(isChecked);
                updateCrime();
            }
        });

        m_suspectButton = (Button) v.findViewById(R.id.choose_suspect);
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        m_suspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(m_crime.getSuspect() != null) {
            m_suspectButton.setText(m_crime.getSuspect());
        }

        m_reportButton = (Button) v.findViewById(R.id.send_report);
        m_reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder shareIntent = ShareCompat.IntentBuilder.from(getActivity());
                shareIntent.setType("text/plain");
                shareIntent.setText(getCrimeReport());
                shareIntent.setSubject(getString(R.string.crime_report_subject));
                shareIntent.startChooser();
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                i.putExtra(Intent.EXTRA_SUBJECT,
//                        getString(R.string.crime_report_subject));
//                i = Intent.createChooser(i, getString(R.string.send_report));
//                startActivity(i);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            m_suspectButton.setEnabled(false);
        }

        m_photoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = m_photoFile != null && captureImage.resolveActivity(packageManager) != null;
        m_photoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(m_photoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        m_photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        m_photoView = (ImageView) v.findViewById(R.id.crime_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_crime_pager, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity())
                .updateCrime(m_crime);
    }

    private void deleteCrime() {
        //get the crime list and remove the crime from it
        CrimeLab.getInstance(getActivity()).removeCrime(m_crime);
        m_callbacks.onCrimeDeleted();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //why even do this if we are only looking for request date...would it be better to have return at the end of the code?
        //maybe this is just good practice

        if (requestCode == REQUEST_DATE || requestCode == REQUEST_TIME){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE_TIME);
            Log.i(CRIME_LOG, "Request Date confirmed, sending extra!: "
                            + date.toString()
            );
            m_crime.setDate(date);
            updateCrime();
        }
        else if (requestCode == REQUEST_CONTACT && data != null) {
            Log.i(CRIME_LOG, "Request Contact confirmed, adding data");

            Uri contactUri = data.getData();
            // specify which fields you want your query to return values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // perform your query - the contactUri is like a "where" clause here
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                //Pull out the first column of the first row of data = that is your suspect's name.
                c.moveToFirst();
                String suspect = c.getString(0);
                m_crime.setSuspect(suspect);
                m_suspectButton.setText(suspect);
                updateCrime();
            } finally {
                c.close();
            }
        }
        else if(requestCode == REQUEST_PHOTO) {
            Log.i(CRIME_LOG, "Photo has been obtained");
            updatePhotoView();
            updateCrime();
        }
        else {
            Log.w(CRIME_LOG, "Result code is not available...");
            return;
        }

        if(requestCode == REQUEST_DATE) {
            updateDate();
        }

        if(requestCode == REQUEST_TIME) {
            updateTime();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.menu_item_delete_crime:
                deleteCrime();
                updateCrime();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateDate() {
        m_dateButton.setText(m_dateFormatter.format(m_crime.getDate()));
    }

    private void updateTime() {
        m_timeButton.setText(m_timeFormatter.format(m_crime.getDate()));
    }

    private void updatePhotoView() {
        if (m_photoFile == null || !m_photoFile.exists()) {
            m_photoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    m_photoFile.getPath(), getActivity());
            m_photoView.setImageBitmap(bitmap);
        }
    }

    private String getCrimeReport() {
        // we will append string together from our string.xml to make a statement that makes sense
        String solvedString = null;
        if (m_crime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        CharSequence dateFormat = "EEE, MMM dd";
        CharSequence dateCharSeq = DateFormat.format(dateFormat, m_crime.getDate());

        String suspect = m_crime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, m_crime.getTitle(), dateCharSeq.toString(), solvedString, suspect);

        return report;
    }
}
