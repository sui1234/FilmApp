package com.example.filmapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieDetails> myMovieDetails;
    private Context context;

    public MovieAdapter(List<MovieDetails> movieDetails, Context context) {
        this.myMovieDetails = movieDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_movie_info, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, int i) {
        MovieDetails movieDetails = myMovieDetails.get(i);

        //viewHolder.the_poster.setImageURI(movieDetails.getThe_poster());
        viewHolder.the_title.setText((CharSequence) movieDetails.getThe_title());
        viewHolder.the_release_date.setText((CharSequence) movieDetails.getThe_release_date());
        viewHolder.the_genre.setText((CharSequence) movieDetails.getThe_genre());
        viewHolder.the_runtime.setText((CharSequence) movieDetails.getThe_runtime());
        viewHolder.the_rating.setText((CharSequence) movieDetails.getThe_raiting());
        viewHolder.the_description.setText((CharSequence) movieDetails.getThe_decription());
        viewHolder.the_cast.setText((CharSequence) movieDetails.getThe_cast());
        viewHolder.the_crew.setText((CharSequence) movieDetails.getThe_crew());
        viewHolder.the_related_movies.setText((CharSequence) movieDetails.getThe_related_movies());
    }

    @Override
    public int getItemCount() {
        return myMovieDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView the_poster;
        public TextView the_title;
        public TextView the_release_date;
        public TextView the_genre;
        public TextView the_runtime;
        public TextView the_rating;
        public TextView the_description;
        public TextView the_cast;
        public TextView the_crew;
        public TextView the_related_movies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            the_poster = itemView.findViewById(R.id.poster);
            the_title = itemView.findViewById(R.id.title);
            the_release_date = itemView.findViewById(R.id.release);
            the_genre = itemView.findViewById(R.id.genres);
            the_runtime = itemView.findViewById(R.id.runtime);
            the_rating = itemView.findViewById(R.id.rating);
            the_description = itemView.findViewById(R.id.description);
            the_cast = itemView.findViewById(R.id.cast);
            the_crew = itemView.findViewById(R.id.crew);
            the_related_movies = itemView.findViewById(R.id.relatedMovies);
        }
    }
}
