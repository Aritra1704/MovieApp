package com.arpaul.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.arpaul.movieapp.DataObject.MovieReviewDO;
import com.arpaul.movieapp.Listener.DynamicHeight;
import com.arpaul.movieapp.R;
import com.arpaul.movieapp.TheMovieAPI.MovieAPI;

import java.util.ArrayList;

/**
 * Created by ARPaul on 01-01-2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private Context context;
    private ArrayList<MovieReviewDO> arrMovieReview;
    public ReviewAdapter(Context context, ArrayList<MovieReviewDO> arrMovieReview) {
        this.context = context;
        this.arrMovieReview = arrMovieReview;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthor;
        ImageView ivShare;
        View view_Cell;
        public ReviewHolder(View view) {
            super(view);
            view_Cell   = view;
            tvAuthor    =   (TextView) view.findViewById(R.id.tvAuthor);
            ivShare      =   (ImageView) view.findViewById(R.id.ivShare);
        }
    }

    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.review_author,parent,false);
        ReviewHolder rvHolder = new ReviewHolder(convertView);
        return rvHolder;
    }

    @Override
    public int getItemCount() {
        if(arrMovieReview != null)
            return arrMovieReview.size();
        return 0;
    }

    @Override
    public void onBindViewHolder(final ReviewAdapter.ReviewHolder holder,final int position) {
        final MovieReviewDO movieReviewDO = arrMovieReview.get(position);
        holder.tvAuthor.setText(movieReviewDO.AUTHOR);

        holder.view_Cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullReviewPopup(movieReviewDO);
            }
        });
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, movieReviewDO.CONTENT);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }

    private void showFullReviewPopup(final MovieReviewDO movieReviewDO) {
        View view            =   LayoutInflater.from(context).inflate(R.layout.review_cell,null,false);
        TextView tvAuthor    =   (TextView) view.findViewById(R.id.tvAuthor);
        TextView tvContent   =   (TextView) view.findViewById(R.id.tvContent);
        TextView tvLink      =   (TextView) view.findViewById(R.id.tvLink);

        tvAuthor.setText(movieReviewDO.AUTHOR);
        tvContent.setText(movieReviewDO.CONTENT);
        tvLink.setText(movieReviewDO.LINK);

        tvContent.setMovementMethod(new ScrollingMovementMethod());

        PopupWindow popupWindow = new PopupWindow(context);

        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(view, Gravity.CENTER, 20, 60);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void refresh(ArrayList<MovieReviewDO> arrMovieReview) {
        this.arrMovieReview = arrMovieReview;
        notifyDataSetChanged();
    }
}
