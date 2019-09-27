package com.example.filmapp;


public class MovieDetails {

    // public ImageView the_poster;
    public String the_poster;
    public String the_title;
    public String the_release_date;
    public String the_description;

    public MovieDetails(String the_poster, String the_title, String the_release_date, String the_description) {
        this.the_poster = the_poster;
        this.the_title = the_title;
        this.the_release_date = the_release_date;
        this.the_description = the_description;
    }

    public String getThe_poster() {
        return the_poster;
    }

    public void setThe_poster(String the_poster) {
        this.the_poster = the_poster;
    }

    public String getThe_title() {
        return the_title;
    }

    public void setThe_title(String the_title) {
        this.the_title = the_title;
    }

    public String getThe_release_date() {
        return the_release_date;
    }

    public void setThe_release_date(String the_release_date) {
        this.the_release_date = the_release_date;
    }

    public String getThe_description() {
        return the_description;
    }

    public void setThe_description(String the_description) {
        this.the_description = the_description;
    }
}
