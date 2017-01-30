package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by RBanks on 9/13/2016.
 */
public class Tools {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isGreaterThanVerstion(int version){
        //android.os.Build.VERSION_CODES.LOLLIPOP is an example of a version param
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= version){
            return true;
        } else{
            return false;
        }
    }
}
