package com.code.publicando.publicando.clases;

public class Post {
    public Integer IdUser;
    public Integer IdPost;
    public String TypeWork;
    public String WorkDetail;
    public byte[] Bitmap;
    public String Image64;
    public Integer Radius ;
    public String Latitude ;
    public String Longitude ;
    public String Celular ;
    public String Phone ;
    public String Description ;
    public Integer Favorite;
    public String Partido ;
    public String Localidad ;
    public String Calle ;
    public Integer Altura;

    public String getLocalidad() {
        return Localidad;
    }

    public void setLocalidad(String localidad) {
        Localidad = localidad;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String calle) {
        Calle = calle;
    }

    public Integer getAltura() {
        return Altura;
    }

    public void setAltura(Integer altura) {
        Altura = altura;
    }

    public String getPartido() {
        return Partido;
    }

    public void setPartido(String partido) {
        Partido = partido;
    }

    public Post()
    {

    }
    public Integer getIdUser() {
        return IdUser;
    }

    public void setIdUser(Integer idUser) {
        IdUser = idUser;
    }

    public Integer getIdPost() {
        return IdPost;
    }

    public void setIdPost(Integer idPost) {
        IdPost = idPost;
    }

    public String getTypeWork() {
        return TypeWork;
    }

    public void setTypeWork(String typeWork) {
        TypeWork = typeWork;
    }

    public String getWorkDetail() {
        return WorkDetail;
    }

    public void setWorkDetail(String workDetail) {
        WorkDetail = workDetail;
    }

    public byte[] getBitmap() {
        return Bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        Bitmap = bitmap;
    }

    public String getImage64() {
        return Image64;
    }

    public void setImage64(String image64) {
        Image64 = image64;
    }

    public Integer getRadius() {
        return Radius;
    }

    public void setRadius(Integer radius) {
        Radius = radius;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String ImageUrl ;

    public Integer getFavorite(){return this.Favorite;}

    public void setFavorite(Integer favorite)
    {
        this.Favorite = favorite;
    }
}
