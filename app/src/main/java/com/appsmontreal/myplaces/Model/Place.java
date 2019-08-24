package com.appsmontreal.myplaces.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable  {
    private String address;
    private double latitude;
    private double longitude;
    public static ArrayList<Place> places = new ArrayList<>();
    private MemorablePlace memorablePlace;


    public Place(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        places.add(this);
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return  address;
    }
}
