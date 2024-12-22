package com.tarotapp;

public class TarotCard {
    private String name;
    private String meaning;
    private String imageUrl;

    public TarotCard(String name, String meaning, String imageUrl) {
        this.name = name;
        this.meaning = meaning;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}