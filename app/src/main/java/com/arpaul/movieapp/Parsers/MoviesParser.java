package com.arpaul.movieapp.Parsers;

import com.arpaul.movieapp.DataObject.MovieDetailDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Created by ARPaul on 01-01-2016.
 */
public class MoviesParser {
    // JSON Node names
    public static final String TAG_PAGE = "page";
    public static final String TAG_RESULTS = "results";

    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_IMAGE_PATH = "backdrop_path";
    public static final String TAG_ADULT = "adult";
    public static final String TAG_GENRE_ID = "genre_ids";
    public static final String TAG_ORIGINAL_LANGUAGE = "original_language";
    public static final String TAG_ORIGINAL_TITLE = "original_title";
    public static final String TAG_OVERVIEw = "overview";
    public static final String TAG_RELASE_DATEE = "release_date";
    public static final String TAG_POSTER_PATH = "poster_path";
    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_VIDEO = "video";
    public static final String TAG_VOTE_AVERAGE = "vote_average";
    public static final String TAG_VOTE_COUNT = "vote_count";

    public LinkedHashMap<String,MovieDetailDO> readPopularMoviesJSONData(String data) {
        LinkedHashMap<String,MovieDetailDO> arrMovies = new LinkedHashMap<>();
        MovieDetailDO objMovieDetailDO = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray result = jsonObject.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < result.length(); i++) {
                JSONObject body = result.getJSONObject(i);

                objMovieDetailDO = new MovieDetailDO();

                String adult                        = body.getString(TAG_ADULT);
                objMovieDetailDO.IMAGE_PATH         = body.getString(TAG_IMAGE_PATH);
                String genreID                      = body.getString(TAG_GENRE_ID);
                objMovieDetailDO.ID                 = body.getString(TAG_ID);
                objMovieDetailDO.ORIGINAL_LANGUAGE  = body.getString(TAG_ORIGINAL_LANGUAGE);
                objMovieDetailDO.ORIGINAL_TITLE     = body.getString(TAG_ORIGINAL_TITLE);
                objMovieDetailDO.OVERVIEw           = body.getString(TAG_OVERVIEw);
                objMovieDetailDO.RELASE_DATEE       = body.getString(TAG_RELASE_DATEE);
                objMovieDetailDO.POSTER_PATH        = body.getString(TAG_POSTER_PATH);
                objMovieDetailDO.POPULARITY         = body.getString(TAG_POPULARITY);
                objMovieDetailDO.TITLE              = body.getString(TAG_TITLE);
                objMovieDetailDO.VIDEO              = body.getString(TAG_VIDEO);
                objMovieDetailDO.VOTE_AVERAGE       = body.getString(TAG_VOTE_AVERAGE);
                objMovieDetailDO.VOTE_COUNT         = body.getString(TAG_VOTE_COUNT);

                /*arrMovies.add(objMovieDetailDO);*/
                arrMovies.put(objMovieDetailDO.ID,objMovieDetailDO);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return arrMovies;
    }
}
