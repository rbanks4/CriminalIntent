package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by RBanks on 8/22/2016.
 */
public class Crime {
    private UUID    m_ID;
    private String  m_title;
    private Date    m_date;
    private boolean m_solved;
    private String  m_suspect;

    public String getSuspect() {
        return m_suspect;
    }

    public void setSuspect(String suspect) {
        m_suspect = suspect;
    }

    public Crime() {
        this(UUID.randomUUID());
//        // Generate unique identifier
//        m_ID = UUID.randomUUID();
//        m_date = new Date();
    }

    public Crime(UUID id) {
        m_ID = id;
        m_date = new Date();
    }

    public UUID getID() {
        return m_ID;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        m_title = title;
    }

    public boolean isSolved() {
        return m_solved;
    }

    public void setSolved(boolean solved) {
        m_solved = solved;
    }

    public Date getDate() {
        return m_date;
    }

    public void setDate(Date date) {
        m_date = date;
    }

    public String getPhotoFilename() { return "IMG_" + getID().toString() + ".jpg"; }
}
