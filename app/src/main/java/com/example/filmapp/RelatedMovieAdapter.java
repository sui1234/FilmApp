package com.example.filmapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;


public class RelatedMovieAdapter extends RecyclerView.Adapter<RelatedMovieAdapter.ViewHolder> {
    Context context;
    ArrayList<MovieItem> movieList;
    private OnMovieListener onMovieListener;

    public RelatedMovieAdapter(Context context, ArrayList<MovieItem> movieList, OnMovieListener onMovieListener) {
        this.context = context;
        this.movieList = movieList;
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.relative_movie_layout, viewGroup, false);
        return new ViewHolder(v, onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MovieItem currentItem = movieList.get(i);

        String title = currentItem.getTitle();
        String posterUrl = currentItem.getPosterImageUrl();

        viewHolder.title.setText(title);
        Glide.with(context)
                .load(posterUrl)
                .centerCrop()
                .into(viewHolder.poster);


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        TextView title;
        OnMovieListener onMovieListener;

        public ViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            this.onMovieListener = onMovieListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: sssss"+movieList.get(getAdapterPosition()).getId());
            final MovieItem currentItem = movieList.get(getAdapterPosition());
            Intent intent = new Intent (view.getContext(), MovieInfoActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("movie_id", String.valueOf(currentItem.getId()));
            intent.putExtras(mBundle);
            context.startActivity(intent);
        }
    }

    public interface OnMovieListener {
        void onMovieClick(int position);
    }

}
