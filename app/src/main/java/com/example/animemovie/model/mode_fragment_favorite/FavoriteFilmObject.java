package com.example.animemovie.model.mode_fragment_favorite;

public class FavoriteFilmObject {
    private String image;
    private String nameFilm;
    private String url;

    public FavoriteFilmObject() {
    }

    public FavoriteFilmObject(String image, String nameFilm, String url) {
        this.image = image;
        this.nameFilm = nameFilm;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNameFilm() {
        return nameFilm;
    }

    public void setNameFilm(String nameFilm) {
        this.nameFilm = nameFilm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
