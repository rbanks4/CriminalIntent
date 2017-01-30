package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeBaseHelper;
import database.CrimeDbSchema;
import database.CrimeDbSchema.CrimeTable;

/**
 * Created by RBanks on 8/23/2016.
 */
public class CrimeLab {
    private static CrimeLab s_crimeLab;

    //private List<Crime> m_crimeList;
    private int m_currSelected;
    private Context m_context;
    private SQLiteDatabase m_database;

    //TODO the fact that we pass in a context when we don't really use it kinda pisses me off; make sure to delete it if it's not needed
    private CrimeLab(Context context){
        //constructor
        //m_crimeList = new ArrayList<>();

        m_context = context.getApplicationContext();
        //use this line of code if the database is never deleted correctly for some reason...sometimes uninstall doesn't work and you need to find another way
        //this only took me 2 hours to find :D.....:D:D:D:D:D (kill me)
        //m_context.deleteDatabase("crimeBase.db");
        m_database = new CrimeBaseHelper(m_context)
                .getWritableDatabase();
        //code below was used to auto-generate 100 crimes to the crime list...we no longer need this
        //as a wise lunch lady once said, "ain't no mo'!"
//        for(int i = 0; i<100; i++){
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i%2 == 0);
//            m_crimeList.add(crime);
//        }
    }
    public static CrimeLab getInstance(Context context){
        if (s_crimeLab == null) {
            s_crimeLab = new CrimeLab(context);
        }
        return s_crimeLab;
    }

    public List<Crime> getCrimeList() {

        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getID().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved()? 1: 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void setSelected (UUID id) {
        int i = getPosition(id);
        m_currSelected = i;
    }

    public int getSelected() {
        return m_currSelected;
    }

    public int getPosition(UUID id){
        int i = 0;
//        for (Crime crime : m_crimeList) {
//            if (crime.getID().equals(id))
//                return i;
//            else
//                i++;
//        }
        return 0;
    }

    public void addCrime(Crime c) {
        //m_crimeList.add(c);
        ContentValues values = getContentValues(c);

        m_database.insert(CrimeTable.NAME, null, values);
    }

    public void removeCrime(Crime c) {
        ContentValues values = getContentValues (c);
        String uuidString = c.getID().toString();

        m_database.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void updateCrime(Crime c) {
        String uuidString = c.getID().toString();
        ContentValues values = getContentValues(c);

        m_database.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{ uuidString });
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = m_database.query(
                CrimeTable.NAME,
                null, //Columns - null selects all of them
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null  //orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }

    //return the files on the filesystem
    public File getPhotoFile(Crime crime) {
        File externalFilesDir = m_context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }
}
