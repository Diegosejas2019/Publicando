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
    private int favorite;
    private String imageUrl;
    private String Type;
    private int Altura;
    private String Calle;
    private String Localidad;
    private String Partido;
    private Double Latitude;
    private Double Longuitude;

    public Product(int id, String title, String shortdesc, String image, int favorite,String Type) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        //this.rating = rating;
        //this.image = image;
        this.imageUrl = image;
        this.favorite = favorite;
        this.Type = Type;
    }

    public Product(int id, String title, String shortdesc, String image, int favorite,String Type,String Localidad,
                   int Altura,String Calle,String Partido,Double Latitude, Double Longuitude) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        //this.rating = rating;
        //this.image = image;
        this.imageUrl = image;
        this.favorite = favorite;
        this.Type = Type;
        this.Localidad = Localidad;
        this.Altura = Altura;
        this.Calle = Calle;
        this.Partido = Partido;
        this.Latitude = Latitude;
        this.Longuitude = Longuitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLonguitude() {
        return Longuitude;
    }

    public void setLonguitude(Double longuitude) {
        Longuitude = longuitude;
    }

    public int getAltura() {
        return Altura;
    }

    public void setAltura(int altura) {
        Altura = altura;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public String getPartido() {
        return Partido;
    }

    public void setPartido(String partido) {
        Partido = partido;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return Type;
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

    public int getFavorite(){
        return this.favorite;
    }
}

