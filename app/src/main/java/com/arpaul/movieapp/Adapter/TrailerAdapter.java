package com.arpaul.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.arpaul.movieapp.DataObject.MovieTrailerDO;
import com.arpaul.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ARPaul on 01-01-2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private Context context;
    private ArrayList<MovieTrailerDO> arrMovieTrailer;

    private String thumbnailPath = "http://img.youtube.com/vi/%s/0.jpg";
    private String youtubeURL = "http://www.youtube.com/watch?v=%s";

    public TrailerAdapter(Context context, ArrayList<MovieTrailerDO> arrMovieTrailer) {
        this.context = context;
        this.arrMovieTrailer = arrMovieTrailer;
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {

        View view_Cell;
        ImageView ivPlay;
        ImageView ivShare;
        public TrailerHolder(View view) {
            super(view);
            view_Cell   =    view;
            ivPlay      =   (ImageView) view.findViewById(R.id.ivPlay);
            ivShare     =   (ImageView) view.findViewById(R.id.ivShare);
        }
    }

    @Override
    public TrailerAdapter.TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.ivtrailer,parent,false);
        TrailerHolder rvHolder = new TrailerHolder(convertView);
        return rvHolder;
    }

    @Override
    public int getItemCount() {
        if(arrMovieTrailer != null)
            return arrMovieTrailer.size();
        return 0;
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.TrailerHolder holder,final int position) {
        final MovieTrailerDO movieTrailerDO = arrMovieTrailer.get(position);
        //holder.tvAuthor.setText(movieTrailerDO.Name);

        if(thumbnailPath != null && !TextUtils.isEmpty(movieTrailerDO.Key))
            Picasso.with(context).load(String.format(thumbnailPath, movieTrailerDO.Key)).into(holder.ivPlay);

        holder.view_Cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullTrailerPopup(movieTrailerDO);
            }
        });
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, String.format(youtubeURL, movieTrailerDO.Key)/*Uri.parse(String.format(youtubeURL,movieTrailerDO.Key))*/);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    private void showFullTrailerPopup(MovieTrailerDO movieTrailerDO) {
        if(movieTrailerDO.Site.equalsIgnoreCase("YouTube")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(youtubeURL,movieTrailerDO.Key)));
            context.startActivity(intent);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void refresh(ArrayList<MovieTrailerDO> arrMovieTrailer) {
        this.arrMovieTrailer = arrMovieTrailer;
        notifyDataSetChanged();
    }
}
