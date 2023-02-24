package com.example.animemovie.model;

import java.io.Serializable;

public class modelIntro implements Serializable {

    private String text;
    private int img;

    public modelIntro(String text, int img) {
        this.text = text;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
