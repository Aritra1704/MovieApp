package com.arpaul.movieapp;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.arpaul.movieapp.Adapter.GridAdapter;
import com.arpaul.movieapp.DataAccess.MoviesCPConstants;
import com.arpaul.movieapp.DataObject.MovieDetailDO;
import com.arpaul.movieapp.Listener.DataListener;
import com.arpaul.movieapp.Parsers.MoviesParser;
import com.arpaul.movieapp.TheMovieAPI.MovieAPI;
import com.arpaul.movieapp.Utilities.NetworkUtility;

import java.util.LinkedHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements DataListener, LoaderManager.LoaderCallbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public boolean mTwoPane;
    private GridAdapter adapter;
    private MovieAPI movieAPI;
    private int TYPE = MovieAPI.TYPE_POPULAR_MOVIES;
    private LinkedHashMap<String,MovieDetailDO> arrMoviesData;

    @InjectView(R.id.movie_list) RecyclerView recyclerView;
    @InjectView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Pop Movies");
        //toolbar.setTitle(getTitle());

        assert recyclerView != null;
        setupGridRecycler();
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (savedInstanceState != null) {
            TYPE = savedInstanceState.getInt("Movie_TYPE", MovieAPI.TYPE_POPULAR_MOVIES);
            setTYPE(TYPE);
        } else {
            setTYPE(MovieAPI.TYPE_POPULAR_MOVIES);
        }

        getSupportLoaderManager().initLoader(1, null, MovieListActivity.this);
        /*initCursorLoader();*/

        movieAPI = new MovieAPI(this);
        loadData();
    }

    private void setTYPE(int type){
        if(NetworkUtility.isConnectionAvailable(MovieListActivity.this))
            TYPE = type;
        else
            TYPE = MovieAPI.TYPE_FAVOURITES;
    }

    private void loadData() {
        if(TYPE != MovieAPI.TYPE_FAVOURITES)
            movieAPI.getPopularMoviesJSON(TYPE);
        else
            getSupportLoaderManager().restartLoader(1, null, MovieListActivity.this);
    }

    public void DataRetrieved(final String data, int type, int status) {
        if(status == MovieAPI.get_STATUS_SUCCESS() && data != null) {
            arrMoviesData = new MoviesParser().readPopularMoviesJSONData(data);
            showGrid(arrMoviesData);
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Uri CONTENT_URI = MoviesCPConstants.CONTENT_URI;
        return new CursorLoader(this, CONTENT_URI, new String[]{MoviesParser.TAG_ID,
                MoviesParser.TAG_TITLE,
                MoviesParser.TAG_IMAGE_PATH,
                MoviesParser.TAG_ADULT,
                MoviesParser.TAG_GENRE_ID,
                MoviesParser.TAG_ORIGINAL_LANGUAGE,
                MoviesParser.TAG_ORIGINAL_TITLE,
                MoviesParser.TAG_OVERVIEw,
                MoviesParser.TAG_RELASE_DATEE,
                MoviesParser.TAG_POSTER_PATH,
                MoviesParser.TAG_POPULARITY,
                MoviesParser.TAG_VIDEO,
                MoviesParser.TAG_VOTE_AVERAGE,
                MoviesParser.TAG_VOTE_COUNT}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(data instanceof Cursor) {
            Cursor cursor = (Cursor) data;
            LinkedHashMap<String,MovieDetailDO> arrMovies = new LinkedHashMap<>();

            if(cursor != null && cursor.moveToFirst()) {
                do {
                    MovieDetailDO objMovieDetailDO = new MovieDetailDO();
                    objMovieDetailDO.ID = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_ID));
                    objMovieDetailDO.TITLE = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_TITLE));
                    objMovieDetailDO.IMAGE_PATH = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_IMAGE_PATH));
                    objMovieDetailDO.ADULT = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_ADULT));
                    objMovieDetailDO.GENRE_ID = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_GENRE_ID));
                    objMovieDetailDO.ORIGINAL_LANGUAGE = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_ORIGINAL_LANGUAGE));
                    objMovieDetailDO.ORIGINAL_TITLE = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_ORIGINAL_TITLE));
                    objMovieDetailDO.OVERVIEw = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_OVERVIEw));
                    objMovieDetailDO.RELASE_DATEE = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_RELASE_DATEE));
                    objMovieDetailDO.POSTER_PATH = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_POSTER_PATH));
                    objMovieDetailDO.POPULARITY = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_POPULARITY));
                    objMovieDetailDO.VIDEO = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_VIDEO));
                    objMovieDetailDO.VOTE_AVERAGE = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_VOTE_AVERAGE));
                    objMovieDetailDO.VOTE_COUNT = cursor.getString(cursor.getColumnIndex(MoviesParser.TAG_VOTE_COUNT));
                    arrMovies.put(objMovieDetailDO.ID,objMovieDetailDO);
                } while(cursor.moveToNext());
            }
            showGrid(arrMovies);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private void showGrid(final LinkedHashMap<String,MovieDetailDO> arrMovies) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(arrMovies != null && arrMovies.size() > 0) {
                    adapter.refresh(arrMovies);
                } else {
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Movie_TYPE", TYPE);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setupGridRecycler();
        /*startFragment();*/
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new GridAdapter(MovieListActivity.this,getSupportFragmentManager(),new LinkedHashMap<String,MovieDetailDO>());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movies_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.popularity) {
            TYPE = MovieAPI.TYPE_POPULAR_MOVIES;
        } else if (id == R.id.release) {
            TYPE = MovieAPI.TYPE_RELEASE_DATE;
        } else if (id == R.id.rated) {
            TYPE = MovieAPI.TYPE_VOTE_COUNT;
        } else if (id == R.id.revenue) {
            TYPE = MovieAPI.TYPE_REVENUE;
        } else if (id == R.id.favourites) {
            TYPE = MovieAPI.TYPE_FAVOURITES;
        } else {
            TYPE = MovieAPI.TYPE_POPULAR_MOVIES;
        }
        loadData();

        return super.onOptionsItemSelected(item);
    }

    private void setupGridRecycler() {
        //if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(MovieListActivity.this, 2));
        /*}
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(MovieListActivity.this, 3));
        }*/
    }
}
