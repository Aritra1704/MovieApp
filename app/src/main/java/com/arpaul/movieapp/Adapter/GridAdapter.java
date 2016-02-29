package com.arpaul.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arpaul.movieapp.DataObject.MovieDetailDO;
import com.arpaul.movieapp.MovieDetailActivity;
import com.arpaul.movieapp.MovieDetailFragment;
import com.arpaul.movieapp.MovieListActivity;
import com.arpaul.movieapp.R;
import com.arpaul.movieapp.TheMovieAPI.MovieAPI;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;
import java.util.Vector;

/**
 * Created by ARPaul on 23-01-2016.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private Context context;
    private FragmentManager fragmentManager;
    private LinkedHashMap<String,MovieDetailDO> arrMoviesData;
    private Vector<String> vecMoviewID;

    public GridAdapter(Context context, FragmentManager fragmentManager, LinkedHashMap<String, MovieDetailDO> arrMoviesData) {
        this.context=context;
        this.fragmentManager = fragmentManager;
        this.arrMoviesData = arrMoviesData;
    }

    public void refresh(LinkedHashMap<String,MovieDetailDO> arrMoviesData) {
        this.arrMoviesData = arrMoviesData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content, parent, false);
        return new ViewHolder(view);
    }

    public Vector<String> getMovieIds(LinkedHashMap<String,MovieDetailDO> arrMovies) {
        if(vecMoviewID == null)
            vecMoviewID = new Vector<>();
        else
            vecMoviewID.clear();

        vecMoviewID = new Vector<String>(arrMovies.keySet());
        return vecMoviewID;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MovieDetailDO objMovieDetail = arrMoviesData.get(vecMoviewID.get(position));

        String imagePath = MovieAPI.getImageURL(context)+objMovieDetail.POSTER_PATH;
        Picasso.with(context).load(imagePath).into(holder.ivPosterImage);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((MovieListActivity)context).mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(MovieDetailFragment.ARG_MOVIE_DETAIL, objMovieDetail);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);

                    if(context instanceof AppCompatActivity) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.movie_detail_container, fragment).commit();
                    }
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("movieDetail",objMovieDetail);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrMoviesData != null) {
            getMovieIds(arrMoviesData);
            return arrMoviesData.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivPosterImage;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivPosterImage = (ImageView) view.findViewById(R.id.ivPosterImage);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
