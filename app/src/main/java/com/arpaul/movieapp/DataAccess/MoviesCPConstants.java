package com.arpaul.movieapp.DataAccess;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by ARPaul on 07-01-2016.
 */
public class MoviesCPConstants {
    public static final String CONTENT_AUTHORITY = "com.arpaul.movieapp.DataAccess.ContentProviderHelper";

    static final String DATABASE_NAME = "Movies.db";
    public static final String FAVOURITES_TABLE_NAME = "Favourites";
    static final int DATABASE_VERSION = 1;

    public static final String DELIMITER = "/";

    public static final String CONTENT = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT + CONTENT_AUTHORITY);

    /*public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVOURITES_TABLE_NAME).build();*/
    public static final Uri CONTENT_URI = Uri.parse(CONTENT + CONTENT_AUTHORITY + DELIMITER + FAVOURITES_TABLE_NAME);

    public static final String PROVIDER_NAME = CONTENT_AUTHORITY;/*"com.arpaul.latestmovieapp.ContentProviderHelper";*/

    // create cursor of base type directory for multiple entries
    public static final String CONTENT_NAME_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATABASE_NAME;
    // create cursor of base type item for single entry
    public static final String CONTENT_ID_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + DATABASE_NAME;

    public static Uri buildMoviesUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
