package com.arpaul.movieapp.Utilities;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by ARPaul on 15-01-2016.
 */
public class ConfigurationUtility {

    public static boolean isOrientationLandscape(Context context) {
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return true;
        } else {
            return false;
        }
    }
}
