package com.code.publicando.publicando.clases;

/**
 * Created by Belal on 10/18/2017.
 */


public class Product {
    private int id;
    private String title;
    private String shortdesc;
    //private double rating;
    private int image;

    private String imageUrl;

    public Product(int id, String title, String shortdesc, String image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        //this.rating = rating;
        //this.image = image;
        this.imageUrl = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    /*public double getRating() {
        return rating;
    }*/

    public int getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

