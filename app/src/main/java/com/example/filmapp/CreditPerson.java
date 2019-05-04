package com.example.filmapp;

public class CreditPerson {
    private String name;
    private String id;
    private String profileImageUrl;
    private String character;

    public CreditPerson(String name, String id, String profileImageUrl, String character) {
        this.name = name;
        this.id = id;
        this.profileImageUrl = profileImageUrl;
        this.character = character;
    }

    public CreditPerson(String name, String id, String profileImage) {
        this.name = name;
        this.id = id;
        this.profileImageUrl = profileImage;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getCharacter() {
        return character;
    }
}
