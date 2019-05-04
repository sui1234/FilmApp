package com.example.filmapp;

public class MovieItem {
    String title;
    String posterImageUrl;
    String releaseDate;
    String description;

    public MovieItem(String title, String imageUrl, String releaseDate, String description) {
        this.title = title;
        this.posterImageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }
}
