package com.code.publicando.publicando.clases;

public class Servicios {
    private int id;
    private String title;
    private int image;

    public Servicios(int id, String title, int image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public Servicios(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}
