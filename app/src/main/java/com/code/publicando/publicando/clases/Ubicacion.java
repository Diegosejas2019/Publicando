package com.code.publicando.publicando.clases;

import android.os.Parcel;
import android.os.Parcelable;

public class Ubicacion implements Parcelable {
    public Integer IdUbicacion;
    public Integer IdUser;
    public Integer Radius;
    public String Latitude;
    public String Longitude;
    public String Provincia;
    public String CP;
    public String Partido;
    public String Localidad;
    public String Calle;
    public Integer Altura;

    public Ubicacion(){}
    public Ubicacion(Parcel in) {
        if (in.readByte() == 0) {
            IdUbicacion = null;
        } else {
            IdUbicacion = in.readInt();
        }
        if (in.readByte() == 0) {
            IdUser = null;
        } else {
            IdUser = in.readInt();
        }
        if (in.readByte() == 0) {
            Radius = null;
        } else {
            Radius = in.readInt();
        }
        Latitude = in.readString();
        Longitude = in.readString();
        Provincia = in.readString();
        CP = in.readString();
        Partido = in.readString();
        Localidad = in.readString();
        Calle = in.readString();
        if (in.readByte() == 0) {
            Altura = null;
        } else {
            Altura = in.readInt();
        }
    }

    public static final Creator<Ubicacion> CREATOR = new Creator<Ubicacion>() {
        @Override
        public Ubicacion createFromParcel(Parcel in) {
            return new Ubicacion(in);
        }

        @Override
        public Ubicacion[] newArray(int size) {
            return new Ubicacion[size];
        }
    };

    public Integer getIdUbicacion() {
        return IdUbicacion;
    }

    public void setIdUbicacion(Integer idUbicacion) {
        IdUbicacion = idUbicacion;
    }

    public Integer getIdUser() {
        return IdUser;
    }

    public void setIdUser(Integer idUser) {
        IdUser = idUser;
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

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getPartido() {
        return Partido;
    }

    public void setPartido(String partido) {
        Partido = partido;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (IdUbicacion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(IdUbicacion);
        }
        if (IdUser == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(IdUser);
        }
        if (Radius == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Radius);
        }
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Provincia);
        dest.writeString(CP);
        dest.writeString(Partido);
        dest.writeString(Localidad);
        dest.writeString(Calle);
        if (Altura == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Altura);
        }
    }
}
