package com.example.filmapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PopularMoiveAdapter extends RecyclerView.Adapter<PopularMoiveAdapter.PopularMovieViewHolder> {
    private Context context;
    private ArrayList<MovieItem> movieList;
    public PopularMoiveAdapter(Context context, ArrayList<MovieItem> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public PopularMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_item_layout, null, false);
        return new PopularMovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMovieViewHolder popularMovieViewHolder, int i) {
        MovieItem currentItem = movieList.get(i);

        String imageUrl = currentItem.getImageUrl();
        String title = currentItem.getTitle();
        String release = currentItem.getReleaseDate();
        String description = currentItem.getDescription();
        if (i == 1){

        }
        popularMovieViewHolder.movieTitle.setText(title);
        popularMovieViewHolder.movieRelease.setText(release);
        popularMovieViewHolder.movieDescription.setText(description);

        Glide.with(context).load(imageUrl)
                .centerCrop()
                .into(popularMovieViewHolder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class PopularMovieViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePoster;
        public TextView movieTitle;
        public TextView movieRelease;
        public TextView movieName;
        public TextView movieDescription;

        public PopularMovieViewHolder(@NonNull View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.movie_poster);
            movieTitle = itemView.findViewById(R.id.movie_title);
            movieRelease = itemView.findViewById(R.id.movie_release);
            movieDescription = itemView.findViewById(R.id.movie_description);
            movieName = itemView.findViewById(R.id.movieName);


        }
    }
}
