package com.example.filmapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PopularMovieAdapter extends RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder> {
    private Context context;
    private ArrayList<MovieItem> movieList;

    public PopularMovieAdapter(Context context, ArrayList<MovieItem> movieList) {
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
        final MovieItem currentItem = movieList.get(i);

        String imageUrl = currentItem.getPosterImageUrl();
        String title = currentItem.getTitle();
        String release = currentItem.getReleaseDate();
        String description = currentItem.getDescription();

        popularMovieViewHolder.movieTitle.setText(title);
        popularMovieViewHolder.movieRelease.setText(release);
        popularMovieViewHolder.movieDescription.setText(description);

        Glide.with(context).load(imageUrl)
                .centerCrop()
                .into(popularMovieViewHolder.moviePoster);

        popularMovieViewHolder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MovieInfoActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("movie_id", currentItem.getId2());
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class PopularMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        @Override
        public void onClick(View v) {
            //klick på hela viewn
        }
    }
}
