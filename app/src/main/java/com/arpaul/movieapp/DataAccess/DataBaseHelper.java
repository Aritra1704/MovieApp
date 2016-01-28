package com.arpaul.movieapp.DataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arpaul.movieapp.Parsers.MoviesParser;

/**
 * Created by ARPaul on 04-01-2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;

    static final String CREATE_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + MoviesCPConstants.FAVOURITES_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MoviesParser.TAG_ID + " TEXT NOT NULL, " +
                    MoviesParser.TAG_TITLE + " TEXT NOT NULL, " +
                    MoviesParser.TAG_IMAGE_PATH + " TEXT NOT NULL, " +
                    MoviesParser.TAG_ADULT + " TEXT NOT NULL, " +
                    MoviesParser.TAG_GENRE_ID + " TEXT NOT NULL, " +
                    MoviesParser.TAG_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                    MoviesParser.TAG_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                    MoviesParser.TAG_OVERVIEw + " TEXT NOT NULL, " +
                    MoviesParser.TAG_RELASE_DATEE + " TEXT NOT NULL, " +
                    MoviesParser.TAG_POSTER_PATH + " TEXT NOT NULL, " +
                    MoviesParser.TAG_POPULARITY + " TEXT NOT NULL, " +
                    MoviesParser.TAG_VIDEO + " TEXT NOT NULL, " +
                    MoviesParser.TAG_VOTE_AVERAGE + " TEXT NOT NULL, " +
                    MoviesParser.TAG_VOTE_COUNT + " TEXT NOT NULL);";

    DataBaseHelper(Context context){
        super(context, MoviesCPConstants.DATABASE_NAME, null, MoviesCPConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesCPConstants.FAVOURITES_TABLE_NAME);
        onCreate(db);
    }
}
