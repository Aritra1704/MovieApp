package com.arpaul.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An activity representing a single Movie detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MovieListActivity}.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @InjectView(R.id.detail_toolbar) Toolbar toolbar;
    @InjectView(R.id.ivMovieImage) ImageView ivMovieImage;
    @InjectView(R.id.toolbar_layout) CollapsingToolbarLayout toolbar_layout;

    private MovieDetailDO movieDetailDO;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.inject(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if(getIntent().hasExtra("movieDetail"))
            movieDetailDO = (MovieDetailDO) getIntent().getSerializableExtra("movieDetail");

        if(movieDetailDO != null) {
            imagePath    =   MovieAPI.IMAGE_URL_DETAIL + movieDetailDO.IMAGE_PATH;

            if(imagePath != null && !TextUtils.isEmpty(imagePath))
                Picasso.with(MovieDetailActivity.this).load(imagePath).into(ivMovieImage);

            toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            toolbar.setTitle(movieDetailDO.TITLE);
            setSupportActionBar(toolbar);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(MovieDetailFragment.ARG_MOVIE_DETAIL, movieDetailDO);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MovieListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
