package com.example.filmapp;

import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetails {

    public ImageView the_poster;
    public TextView the_title;
    public TextView the_release_date;
    public TextView the_description;

    public MovieDetails(ImageView the_poster, TextView the_title, TextView the_release_date, TextView the_description) {
        this.the_poster = the_poster;
        this.the_title = the_title;
        this.the_release_date = the_release_date;
        this.the_description = the_description;
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

    public TextView getThe_description() {
        return the_description;
    }

    public void setThe_description(TextView the_description) {
        this.the_description = the_description;
    }
}
