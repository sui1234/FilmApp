package com.example.filmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        viewHolder.the_description.setText((CharSequence) movieDetails.getThe_decription());
    }

    @Override
    public int getItemCount() {
        return myMovieDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView the_poster;
        public TextView the_title;
        public TextView the_release_date;
        public TextView the_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            the_poster = itemView.findViewById(R.id.poster);
            the_title = itemView.findViewById(R.id.title);
            the_release_date = itemView.findViewById(R.id.release);
            the_description = itemView.findViewById(R.id.description);
        }
    }
}
