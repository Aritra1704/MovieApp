package com.arpaul.movieapp.DataAccess;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.arpaul.movieapp.Parsers.MoviesParser;

/**
 * Created by ARPaul on 04-01-2016.
 */
public class ContentProviderHelper extends ContentProvider {

    public static final int MOVIES = 1;
    public static final int MOVIES_ID = 2;

    public static final String TAG_ID = "/#";

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesCPConstants.PROVIDER_NAME, MoviesCPConstants.FAVOURITES_TABLE_NAME, MOVIES);
        uriMatcher.addURI(MoviesCPConstants.PROVIDER_NAME, MoviesCPConstants.FAVOURITES_TABLE_NAME + TAG_ID, MOVIES_ID);
    }

    private DataBaseHelper mOpenHelper;

    public static Uri getContentUri(int type) {
        String URL = MoviesCPConstants.CONTENT + MoviesCPConstants.PROVIDER_NAME;
        switch (type) {
            case MOVIES:
                URL += "/"+ MoviesParser.TAG_TITLE;
                break;
            case MOVIES_ID:
                URL += "/"+ MoviesParser.TAG_ID;
                break;
        }
        return Uri.parse(URL);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DataBaseHelper(getContext());
        return (mOpenHelper == null) ? false : true;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numInserted = 0;
        Uri returnURI;
        switch (uriMatcher.match(uri)) {
              case MOVIES: {
                  db.beginTransaction();
                  try {

                      for (ContentValues value : values /*int i = 0; i < values.length; i++*/) {
                          long _id = db.insert(MoviesCPConstants.FAVOURITES_TABLE_NAME, null, value);
                          if (_id > 0) {
                              returnURI = MoviesCPConstants.buildMoviesUri(_id);
                              numInserted++;
                          } else {
                              throw new SQLException("Failed to insert row into: " + uri);
                          }
                      }
                      if (numInserted == 0) {
                          db.setTransactionSuccessful();
                      }
                  } catch (Exception ex) {
                      ex.printStackTrace();
                  } finally {
                      db.endTransaction();
                  }
                  return numInserted;
              }
              default: {
                  return super.bulkInsert(uri, values);
              }
        }
    }

    /**
     * Create a write able database which will trigger its
     * creation if it doesn't already exist.
     *//*
        mOpenHelper = dbHelper.getWritableDatabase();
    }*/


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        Uri returnURI;
        switch (uriMatcher.match(uri)) {
            case MOVIES: {
                long _id = db.insert(MoviesCPConstants.FAVOURITES_TABLE_NAME, null, values);
                if (_id > 0) {
                    returnURI = MoviesCPConstants.buildMoviesUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnURI;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        String query = "";
        if(selectionArgs != null && selectionArgs.length > 0){
            query = "SELECT "+projection[0]+" FROM "+MoviesCPConstants.FAVOURITES_TABLE_NAME+" WHERE "+MoviesParser.TAG_ID + " = "+selectionArgs[0];
            Log.d("movies_query",query);
        }
        switch (uriMatcher.match(uri)) {

            case MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesCPConstants.FAVOURITES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;

            case MOVIES_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesCPConstants.FAVOURITES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        String query = "";
        if(selection != null && selection.length() > 0){
            query = "DELETE FROM "+MoviesCPConstants.FAVOURITES_TABLE_NAME+" WHERE "+MoviesParser.TAG_ID + " = "+selection;
            Log.d("movies_delete_query",query);
        }

        switch (uriMatcher.match(uri)) {
            case MOVIES:
                count = db.delete(MoviesCPConstants.FAVOURITES_TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIES_ID:
                count = db.delete(MoviesCPConstants.FAVOURITES_TABLE_NAME,
                        MoviesParser.TAG_ID + " = " + selection +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MOVIES:
                count = db.update(MoviesCPConstants.FAVOURITES_TABLE_NAME, values, selection, selectionArgs);
                break;

            case MOVIES_ID:
                count = db.update(MoviesCPConstants.FAVOURITES_TABLE_NAME, values, MoviesParser.TAG_ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIES: {
                return MoviesCPConstants.CONTENT_NAME_TYPE;
            }
            case MOVIES_ID: {
                return MoviesCPConstants.CONTENT_ID_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
}