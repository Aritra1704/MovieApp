package com.arpaul.movieapp.Parsers;

import com.arpaul.movieapp.DataObject.MovieTrailerDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ARPaul on 01-01-2016.
 */
public class MoviesTrailerParser {
    public static final String TAG_PAGE = "page";
    public static final String TAG_RESULTS = "results";

    public static final String TAG_ID = "id";
    public static final String TAG_ISO = "iso_639_1";
    public static final String TAG_KEY = "key";
    public static final String TAG_NAME = "name";
    public static final String TAG_SITE = "site";
    public static final String TAG_SIZE = "size";
    public static final String TAG_TYPE = "type";

    public ArrayList<MovieTrailerDO> readMoviesTrailersJSONData(String data) {
        ArrayList<MovieTrailerDO> arrMovies = new ArrayList<>();
        MovieTrailerDO objMovieDetailDO = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray result = jsonObject.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < result.length(); i++) {
                JSONObject body = result.getJSONObject(i);

                objMovieDetailDO = new MovieTrailerDO();

                objMovieDetailDO.Id                  = body.getString(TAG_ID);
                objMovieDetailDO.ISO                 = body.getString(TAG_ISO);
                objMovieDetailDO.Key                 = body.getString(TAG_KEY);
                objMovieDetailDO.Name                = body.getString(TAG_NAME);
                objMovieDetailDO.Site                = body.getString(TAG_SITE);
                objMovieDetailDO.Size                = body.getString(TAG_SIZE);
                objMovieDetailDO.Type                = body.getString(TAG_TYPE);

                arrMovies.add(objMovieDetailDO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return arrMovies;
    }
}
