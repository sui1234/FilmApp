package com.example.filmapp;

import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetails {

    public ImageView the_poster;
    public TextView the_title;
    public TextView the_release_date;
    public TextView the_genre;
    public TextView the_runtime;
    public TextView the_raiting;
    public TextView the_decription;
    public TextView the_cast;
    public TextView the_crew;
    public TextView the_related_movies;

    public MovieDetails(ImageView the_poster, TextView the_title, TextView the_release_date,
                        TextView the_genre, TextView the_runtime, TextView the_raiting,
                        TextView the_decription, TextView the_cast, TextView the_crew,
                        TextView the_related_movies) {
        this.the_poster = the_poster;
        this.the_title = the_title;
        this.the_release_date = the_release_date;
        this.the_genre = the_genre;
        this.the_runtime = the_runtime;
        this.the_raiting = the_raiting;
        this.the_decription = the_decription;
        this.the_cast = the_cast;
        this.the_crew = the_crew;
        this.the_related_movies = the_related_movies;
    }

    public ImageView getThe_poster() {
        return the_poster;
    }

    public void setThe_poster(ImageView the_poster) {
        this.the_poster = the_poster;
    }

    public TextView getThe_title() {
        return the_title;
    }

    public void setThe_title(TextView the_title) {
        this.the_title = the_title;
    }

    public TextView getThe_release_date() {
        return the_release_date;
    }

    public void setThe_release_date(TextView the_release_date) {
        this.the_release_date = the_release_date;
    }

    public TextView getThe_genre() {
        return the_genre;
    }

    public void setThe_genre(TextView the_genre) {
        this.the_genre = the_genre;
    }

    public TextView getThe_runtime() {
        return the_runtime;
    }

    public void setThe_runtime(TextView the_runtime) {
        this.the_runtime = the_runtime;
    }

    public TextView getThe_raiting() {
        return the_raiting;
    }

    public void setThe_raiting(TextView the_raiting) {
        this.the_raiting = the_raiting;
    }

    public TextView getThe_decription() {
        return the_decription;
    }

    public void setThe_decription(TextView the_decription) {
        this.the_decription = the_decription;
    }

    public TextView getThe_cast() {
        return the_cast;
    }

    public void setThe_cast(TextView the_cast) {
        this.the_cast = the_cast;
    }

    public TextView getThe_crew() {
        return the_crew;
    }

    public void setThe_crew(TextView the_crew) {
        this.the_crew = the_crew;
    }

    public TextView getThe_related_movies() {
        return the_related_movies;
    }

    public void setThe_related_movies(TextView the_related_movies) {
        this.the_related_movies = the_related_movies;
    }
}
