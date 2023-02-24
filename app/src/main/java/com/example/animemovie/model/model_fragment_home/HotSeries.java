package com.example.animemovie.model.model_fragment_home;

public class HotSeries {
    private int id;
    private String image;
    private String nameFilm;
    private String url;
    private String urlImageNumber;

    public HotSeries() {
    }

    public HotSeries(int id, String image, String nameFilm, String url, String urlImageNumber) {
        this.id = id;
        this.image = image;
        this.nameFilm = nameFilm;
        this.url = url;
        this.urlImageNumber = urlImageNumber;
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

    public String getUrlImageNumber() {
        return urlImageNumber;
    }

    public void setUrlImageNumber(String urlImageNumber) {
        this.urlImageNumber = urlImageNumber;
    }
}