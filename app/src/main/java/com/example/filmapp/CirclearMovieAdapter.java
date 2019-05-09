package com.example.filmapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jackandphantom.circularimageview.CircleImage;

import java.util.ArrayList;


public class CirclearMovieAdapter extends RecyclerView.Adapter<CirclearMovieAdapter.ViewHolder> {
    private static final String TAG = "CirclearMovieAdapter";
    private Context context;
    private ArrayList<MovieItem> movieListCircle;

    public CirclearMovieAdapter(Context context, ArrayList<MovieItem> movieList) {
        this.context = context;
        this.movieListCircle = movieList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.circle_movie_item, null, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final MovieItem currentItem = movieListCircle.get(i);

        String imageUrl = currentItem.getPosterImageUrl();
        final String title = currentItem.getTitle();

        viewHolder.mMovieName.setText(title);
        Glide.with(context).load(imageUrl)
                .centerCrop()
                .into(viewHolder.mMovieImage);
        viewHolder.mMovieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MovieInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("movie_id", currentItem.getId2());
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieListCircle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mMovieName;
        CircleImage mMovieImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.movieImage);
            mMovieName = itemView.findViewById(R.id.movieName);

        }
    }
}
