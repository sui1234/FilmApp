package com.example.filmapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<MovieDetails> myMovieDetails;
    private Context context;

    public MovieAdapter(ArrayList<MovieDetails> movieDetails, Context context) {
        this.myMovieDetails = movieDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder viewHolder, int i) {
        //MovieDetails movieDetails = myMovieDetails.get(i);

        //viewHolder.the_poster.setImageURI(movieDetails.getThe_poster());

        Log.d("===", "onBindViewHolder: " + viewHolder.the_title);
        try {
            viewHolder.the_title.setText(myMovieDetails.get(i).getThe_title());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        viewHolder.the_release_date.setText(myMovieDetails.get(i).getThe_release_date());
        viewHolder.the_description.setText(myMovieDetails.get(i).getThe_description());
    }

    @Override
    public int getItemCount() {
        return myMovieDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView the_poster;
        public TextView the_title;
        public TextView the_release_date;
        public TextView the_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            the_poster = itemView.findViewById(R.id.movie_poster);
            the_title = itemView.findViewById(R.id.movie_title);
            the_release_date = itemView.findViewById(R.id.movie_release);
            the_description = itemView.findViewById(R.id.movie_description);
        }
    }
}
