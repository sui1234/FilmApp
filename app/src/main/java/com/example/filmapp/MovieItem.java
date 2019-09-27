package com.example.filmapp;

public class MovieItem {
    String title;
    String posterImageUrl;
    String releaseDate;
    String description;
    int id;
    String id2;

    public MovieItem(String title, String imageUrl, String releaseDate, String description, String id) {
        this.title = title;
        this.posterImageUrl = imageUrl;
        this.releaseDate = releaseDate;
        this.description = description;
        this.id2 = id;

    }

    public MovieItem(String title, String imageUrl, int id) {
        this.title = title;
        this.posterImageUrl = imageUrl;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getId2() {
        return id2;
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
