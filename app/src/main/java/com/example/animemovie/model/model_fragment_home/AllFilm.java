package com.example.animemovie.model.model_fragment_home;

public class AllFilm {
    private int id;
    private String image;
    private String nameFilm;
    private String url;

    public AllFilm() {
    }

    public AllFilm(int id, String image, String nameFilm, String url) {
        this.id = id;
        this.image = image;
        this.nameFilm = nameFilm;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
