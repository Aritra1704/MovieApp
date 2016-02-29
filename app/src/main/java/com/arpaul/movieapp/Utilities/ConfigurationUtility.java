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

    public static final int  SCREEN_SIZE_XLARGE     = 3;
    public static final int  SCREEN_SIZE_LARGE      = 2;
    public static final int  SCREEN_SIZE_NORMAL     = 1;
    public static final int  SCREEN_SIZE_SMALL      = 0;

    public static int screenSize(Context context){
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        int resolution = SCREEN_SIZE_SMALL;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                resolution = SCREEN_SIZE_XLARGE;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                resolution = SCREEN_SIZE_LARGE;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                resolution = SCREEN_SIZE_NORMAL;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                resolution = SCREEN_SIZE_SMALL;
                break;
            default:
        }
        return resolution;
    }
}
