package com.business.quickcare;

import com.google.firebase.firestore.GeoPoint;

public class QuickCareProvider {

    private String name;
    private String location;
    private String id;
    private String[] coordinates = new String[2];
    private GeoPoint geoPoint;
    private int price;
    public QuickCareProvider (String name, String location, String id, GeoPoint geoPoint, int price)
    {
        this.name = name;
        this.location = location;
        this.id = id;
        this.geoPoint = geoPoint;
        this.price = price;

    }
    public int getPrice(){return this.price;}
    public String getName()
    {
        return this.name;
    }
    public String getLocation()
    {
        return this.location;
    }
    public String getId(){return this.id;}
    public void setName(String name)
    {
        this.name = name;
    }

    public String[] getCoordinates() {
        return this.coordinates;
    }
    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
    public void setPrice(int price)
    {
        this.price = price;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
    public void setId(String id){this.id = id;}
    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
