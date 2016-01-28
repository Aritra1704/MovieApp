package com.arpaul.movieapp.Parsers;

import com.arpaul.movieapp.DataObject.MovieReviewDO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ARPaul on 01-01-2016.
 */
public class MoviesReviewParser {
    public static final String TAG_PAGE = "page";
    public static final String TAG_RESULTS = "results";

    public static final String TAG_ID = "id";
    public static final String TAG_AUTHOR = "author";
    public static final String TAG_CONTENT = "content";

    public ArrayList<MovieReviewDO> readMoviesReviewsJSONData(String data) {
        ArrayList<MovieReviewDO> arrMovies = new ArrayList<>();
        MovieReviewDO objMovieDetailDO = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray result = jsonObject.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < result.length(); i++) {
                JSONObject body = result.getJSONObject(i);

                objMovieDetailDO = new MovieReviewDO();

                objMovieDetailDO.ID                 = body.getString(TAG_ID);
                objMovieDetailDO.AUTHOR             = body.getString(TAG_AUTHOR);
                objMovieDetailDO.CONTENT             = body.getString(TAG_CONTENT);

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
