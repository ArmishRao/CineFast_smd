package com.example.assignment1_l230504;
public class Movie {
    String name, genre,trailerURL;
    int image;

    public Movie(int image, String name, String genre, String trailerURL) {
        this.image = image;
        this.genre=genre;
        this.name=name;
        this.trailerURL=trailerURL;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public String getGenre() {
        return genre;
    }

    public String getTrailerURL() {
        return trailerURL;
    }
}
