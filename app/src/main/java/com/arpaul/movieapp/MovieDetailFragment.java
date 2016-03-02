package com.arpaul.movieapp;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpaul.movieapp.Adapter.ReviewAdapter;
import com.arpaul.movieapp.Adapter.TrailerAdapter;
import com.arpaul.movieapp.DataAccess.ContentProviderHelper;
import com.arpaul.movieapp.DataAccess.MoviesCPConstants;
import com.arpaul.movieapp.DataObject.MovieDetailDO;
import com.arpaul.movieapp.DataObject.MovieReviewDO;
import com.arpaul.movieapp.DataObject.MovieTrailerDO;
import com.arpaul.movieapp.Listener.DataListener;
import com.arpaul.movieapp.Parsers.MoviesParser;
import com.arpaul.movieapp.Parsers.MoviesReviewParser;
import com.arpaul.movieapp.Parsers.MoviesTrailerParser;
import com.arpaul.movieapp.TheMovieAPI.MovieAPI;
import com.arpaul.movieapp.Utilities.CalendarUtils;
import com.arpaul.movieapp.Utilities.WrappingLinearLayoutManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements DataListener {

    @InjectView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @InjectView(R.id.tvVoteAverage) TextView tvVoteAverage;
    @InjectView(R.id.tvPlotSynopsis) TextView tvPlotSynopsis;
    @InjectView(R.id.tvReview) TextView tvReview;
    @InjectView(R.id.tvTrailer) TextView tvTrailer;
    @InjectView(R.id.btnFavourite) ImageView btnFavourite;
    @InjectView(R.id.rvReview) RecyclerView rvReview;
    @InjectView(R.id.rvTrailer) RecyclerView rvTrailer;

    private String imagePath;
    private MovieAPI movieAPI;
    private ArrayList<MovieReviewDO> arrMovieReview;
    private ArrayList<MovieTrailerDO> arrMovieTrailer;
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    //private Uri CONTENT_URI;

    private MovieDetailDO movieDetailDO;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_MOVIE_DETAIL = "movieDetail";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE_DETAIL)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            movieDetailDO = (MovieDetailDO) getArguments().getSerializable(ARG_MOVIE_DETAIL);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(movieDetailDO.TITLE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_detail, container, false);

        ButterKnife.inject(this, rootView);

        if(movieDetailDO != null) {
            tvReleaseDate.setText(CalendarUtils.getCommaFormattedDate(movieDetailDO.RELASE_DATEE));
            tvVoteAverage.setText("Votes: " + movieDetailDO.VOTE_AVERAGE + "/10");
            tvPlotSynopsis.setText(movieDetailDO.OVERVIEw);
        }

        bindControls();

        movieAPI = new MovieAPI(getActivity(),this);
        movieAPI.getReview(movieDetailDO.ID);
        movieAPI.getTrailers(movieDetailDO.ID);

        return rootView;
    }

    private void bindControls() {
        WrappingLinearLayoutManager layoutManagerReview = new WrappingLinearLayoutManager(getActivity());
        reviewAdapter = new ReviewAdapter(getActivity(),new ArrayList<MovieReviewDO>());
        rvReview.setAdapter(reviewAdapter);
        rvReview.setLayoutManager(layoutManagerReview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        trailerAdapter = new TrailerAdapter(getActivity(),new ArrayList<MovieTrailerDO>());
        rvTrailer.setAdapter(trailerAdapter);
        rvTrailer.setLayoutManager(layoutManager);

        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = null;
                try {
                    cursor = getFavouritesCursor();
                    if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                        Log.d("getContentResolver", "true");
                        Uri CONTENT_URI = Uri.parse(MoviesCPConstants.CONTENT + MoviesCPConstants.CONTENT_AUTHORITY + MoviesCPConstants.DELIMITER + MoviesCPConstants.FAVOURITES_TABLE_NAME + MoviesCPConstants.DELIMITER + movieDetailDO.ID);
                        getActivity().getContentResolver().delete(CONTENT_URI, movieDetailDO.ID, null);
                        setFavourites(false);
                    } else {
                        Log.d("getContentResolver", "false");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MoviesParser.TAG_ID, movieDetailDO.ID);
                        contentValues.put(MoviesParser.TAG_TITLE, movieDetailDO.TITLE);
                        contentValues.put(MoviesParser.TAG_IMAGE_PATH, movieDetailDO.IMAGE_PATH);
                        contentValues.put(MoviesParser.TAG_ADULT, movieDetailDO.ADULT);
                        contentValues.put(MoviesParser.TAG_GENRE_ID, movieDetailDO.GENRE_ID);
                        contentValues.put(MoviesParser.TAG_ORIGINAL_LANGUAGE, movieDetailDO.ORIGINAL_LANGUAGE);
                        contentValues.put(MoviesParser.TAG_ORIGINAL_TITLE, movieDetailDO.ORIGINAL_TITLE);
                        contentValues.put(MoviesParser.TAG_OVERVIEw, movieDetailDO.OVERVIEw);
                        contentValues.put(MoviesParser.TAG_RELASE_DATEE, movieDetailDO.RELASE_DATEE);
                        contentValues.put(MoviesParser.TAG_POSTER_PATH, movieDetailDO.POSTER_PATH);
                        contentValues.put(MoviesParser.TAG_POPULARITY, movieDetailDO.POPULARITY);
                        contentValues.put(MoviesParser.TAG_VIDEO, movieDetailDO.VIDEO);
                        contentValues.put(MoviesParser.TAG_VOTE_AVERAGE, movieDetailDO.VOTE_AVERAGE);
                        contentValues.put(MoviesParser.TAG_VOTE_COUNT, movieDetailDO.VOTE_COUNT);

                        Uri uri = getActivity().getContentResolver().insert(MoviesCPConstants.CONTENT_URI, contentValues);
                        setFavourites(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (cursor != null && !cursor.isClosed())
                        cursor.close();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = null;
        try {
            cursor = getFavouritesCursor();
            if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                Log.d("getContentResolver","true" );
                setFavourites(true);
            } else {
                Log.d("getContentResolver","false" );
                setFavourites(false);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        movieAPI.cancelRequest();
    }

    @Override
    public void DataRetrieved(String data, int Type, int Status) {
        if(Status == MovieAPI.get_STATUS_SUCCESS() && data != null) {
            if(Type == MovieAPI.TYPE_MOVIE_REVIEW) {
                arrMovieReview = new MoviesReviewParser().readMoviesReviewsJSONData(data);
            } else if(Type == MovieAPI.TYPE_MOVIE_TRAILER) {
                arrMovieTrailer = new MoviesTrailerParser().readMoviesTrailersJSONData(data);
            }
            showinUIThread(Type);
        }
    }

    private Cursor getFavouritesCursor() {
        Uri CONTENT_URI = Uri.parse(MoviesCPConstants.CONTENT + MoviesCPConstants.CONTENT_AUTHORITY + MoviesCPConstants.DELIMITER + MoviesCPConstants.FAVOURITES_TABLE_NAME + MoviesCPConstants.DELIMITER + movieDetailDO.ID);
        return getActivity().getContentResolver().query(CONTENT_URI,
                new String[]{MoviesParser.TAG_ID},
                MoviesParser.TAG_ID + " = ?",
                new String[]{movieDetailDO.ID},
                null);
    }

    private void setFavourites(boolean state) {
        if(state)
            btnFavourite.setBackgroundResource(R.drawable.favourite);
            //btnFavourite.setBackgroundColor(getResources().getColor(R.color.colorOffWhite));
        else
            btnFavourite.setBackgroundResource(R.drawable.favourite_h);
            //btnFavourite.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void showinUIThread(final int Type) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Type == MovieAPI.TYPE_MOVIE_REVIEW) {
                    if (arrMovieReview != null && arrMovieReview.size() > 0) {
                        rvReview.setVisibility(View.VISIBLE);
                        tvReview.setVisibility(View.VISIBLE);
                        reviewAdapter.refresh(arrMovieReview);
                    } else {
                        rvReview.setVisibility(View.GONE);
                        tvReview.setVisibility(View.GONE);
                    }
                } else if (Type == MovieAPI.TYPE_MOVIE_TRAILER) {
                    if (arrMovieTrailer != null && arrMovieTrailer.size() > 0) {
                        rvTrailer.setVisibility(View.VISIBLE);
                        tvTrailer.setVisibility(View.VISIBLE);
                        trailerAdapter.refresh(arrMovieTrailer);
                    } else {
                        rvTrailer.setVisibility(View.GONE);
                        tvTrailer.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
