package com.arpaul.movieapp.TheMovieAPI;

import android.content.Context;

import com.arpaul.movieapp.Listener.DataListener;
import com.arpaul.movieapp.R;
import com.arpaul.movieapp.Utilities.ConfigurationUtility;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ARPaul on 25-12-2015.
 */
public class MovieAPI {

    private Context context;
    private DataListener dataListener = null;
    private String API_KEY = "API_KEY";
    private final String MAIN_URL = "http://api.themoviedb.org/3/";

    public final static String IMAGE_URL_SMALL = "http://image.tmdb.org/t/p/w92";
    public final static String IMAGE_URL_MEDIUM = "http://image.tmdb.org/t/p/w154";
    public final static String IMAGE_URL_NORMAL = "http://image.tmdb.org/t/p/w185";
    public final static String IMAGE_URL_LARGE = "http://image.tmdb.org/t/p/w342";
    public final static String IMAGE_URL_XLARGE = "http://image.tmdb.org/t/p/w500";
    public final static String IMAGE_URL_XXLARGE = "http://image.tmdb.org/t/p/w780";

    public final static String IMAGE_URL_DETAIL = IMAGE_URL_XXLARGE;
    public final static String IMAGE_URL_GRID = IMAGE_URL_LARGE;

    private final String POPULARITY = "popularity.desc";
    private final String RELEASE_DATE = "release_date.desc";
    private final String VOTE_COUNT = "vote_count.desc";
    private final String REVENUE = "revenue.desc";
    private final String FAVOURITE = "favourites";

    private final int TIMEOUT = 10000;

    public MovieAPI(Context context,DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;

        API_KEY = context.getString(R.string.api_key);
    }

    public final static int TYPE_POPULAR_MOVIES         = 1;
    public final static int TYPE_RELEASE_DATE           = 2;
    public final static int TYPE_VOTE_COUNT             = 3;
    public final static int TYPE_REVENUE                = 4;
    public final static int TYPE_FAVOURITES             = 5;

    public final static int TYPE_MOVIE_REVIEW           = 101;
    public final static int TYPE_MOVIE_TRAILER          = 102;

    private final static int STATUS_SUCCESS             = 200;
    private final static int STATUS_FAILED              = 500;
    private final static int STATUS_UPDATED_SUCCESS     = 201;
    private final static int STATUS_DELETED_SUCCESS     = 200;
    private final static int STATUS_INVALID_SERVICE     = 501;
    private final static int STATUS_INVALID_API_KEY     = 401;

    public static int get_STATUS_SUCCESS() {
        return STATUS_SUCCESS;
    }

    public static int get_STATUS_FAILED() {
        return STATUS_FAILED;
    }

    public static int get_STATUS_UPDATED_SUCCESS() {
        return STATUS_UPDATED_SUCCESS;
    }

    public static int get_STATUS_DELETED_SUCCESS() {
        return STATUS_DELETED_SUCCESS;
    }

    public static int get_STATUS_INVALID_SERVICE() {
        return STATUS_INVALID_SERVICE;
    }

    public static int get_STATUS_INVALID_API_KEY() {
        return STATUS_INVALID_API_KEY;
    }

    public void getPopularMoviesJSON(final int type,final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sortOrder = "";
                switch(type) {
                    case TYPE_RELEASE_DATE:
                        sortOrder = "sort_by="+RELEASE_DATE+"&";
                        break;
                    case TYPE_VOTE_COUNT:
                        sortOrder = "sort_by="+VOTE_COUNT+"&";
                        break;
                    case TYPE_REVENUE:
                        sortOrder = "sort_by="+REVENUE+"&";
                        break;
                    case TYPE_FAVOURITES:
                        sortOrder = "";
                        break;
                    default :
                        sortOrder = "sort_by="+POPULARITY+"&";
                }
                String popularURL = MAIN_URL+"discover/movie?"+sortOrder+"api_key="+API_KEY+"&page="+page;
                String data = getJSON(popularURL);

                if(data != null)
                    dataListener.DataRetrieved(data, type, STATUS_SUCCESS);
                else
                    dataListener.DataRetrieved("", type, STATUS_FAILED);
            }
        }).start();
    }

    private String getJSON(String URL) {
        HttpURLConnection httpClient = null;
        StringBuilder sb = null;
        try {
            URL url = new URL(URL);
            httpClient = (HttpURLConnection) url.openConnection();
            httpClient.setRequestMethod("GET");
            httpClient.setRequestProperty("Content-length","0");
            httpClient.setUseCaches(false);
            httpClient.setAllowUserInteraction(false);
            httpClient.setConnectTimeout(TIMEOUT);
            httpClient.setReadTimeout(TIMEOUT);
            httpClient.connect();

            int status = httpClient.getResponseCode();
            switch (status) {
                case STATUS_SUCCESS :
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
                    sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
            }
        } catch(MalformedURLException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            ex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(httpClient != null) {
                httpClient.disconnect();
            }
        }
        if(sb != null)
            return sb.toString();

        return null;
    }

    OkHttpClient okHttpClientReview;
    Call callReview;
    public void getReview(String ID) {
        okHttpClientReview = new OkHttpClient();
        String requestURL = MAIN_URL+"movie/"+ID+"/reviews?api_key="+API_KEY;
        Request request = new Request.Builder().url(requestURL).build();
        callReview = okHttpClientReview.newCall(request);
        callReview.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dataListener.DataRetrieved("", TYPE_MOVIE_REVIEW, STATUS_FAILED);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && dataListener != null) {
                    String review = response.body().string();
                    if (review != null)
                        dataListener.DataRetrieved(review, TYPE_MOVIE_REVIEW, STATUS_SUCCESS);
                    else
                        dataListener.DataRetrieved("", TYPE_MOVIE_REVIEW, STATUS_FAILED);
                }
            }
        });
    }

    OkHttpClient okHttpClientTrailers;
    Call callTrailers;
    public void getTrailers(String ID) {
        okHttpClientTrailers = new OkHttpClient();
        String requestURL = MAIN_URL+"movie/"+ID+"/videos?api_key="+API_KEY;
        Request request = new Request.Builder().url(requestURL).build();
        callTrailers = okHttpClientTrailers.newCall(request);
        callTrailers.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dataListener.DataRetrieved("", TYPE_MOVIE_TRAILER, STATUS_FAILED);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful() && dataListener != null) {
                    String review = response.body().string();
                    if (review != null)
                        dataListener.DataRetrieved(review, TYPE_MOVIE_TRAILER, STATUS_SUCCESS);
                    else
                        dataListener.DataRetrieved("", TYPE_MOVIE_TRAILER, STATUS_FAILED);
                }
            }
        });
    }

    public void cancelRequest(){
        /*okHttpClientReview.cancel(null);
        okHttpClientTrailers.cancel(null);*/
        callReview.cancel();;
        callTrailers.cancel();
    }

    public static String getImageURL(Context context){
        switch(ConfigurationUtility.screenSize(context)){
            case ConfigurationUtility.SCREEN_SIZE_SMALL:
                return IMAGE_URL_SMALL;
            case ConfigurationUtility.SCREEN_SIZE_NORMAL:
                return IMAGE_URL_NORMAL;
            case ConfigurationUtility.SCREEN_SIZE_LARGE:
                return IMAGE_URL_LARGE;
            case ConfigurationUtility.SCREEN_SIZE_XLARGE:
                return IMAGE_URL_XLARGE;
            default:
                return IMAGE_URL_XLARGE;
        }
    }
}
