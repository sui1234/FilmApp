package com.example.filmapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

        String imageUrl = currentItem.getImageUrl();
        final String title = currentItem.getTitle();

        viewHolder.mMovieName.setText(title);
        Glide.with(context).load(imageUrl)
                .centerCrop()
                .into(viewHolder.mMovieImage);
        viewHolder.mMovieImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, title);
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
