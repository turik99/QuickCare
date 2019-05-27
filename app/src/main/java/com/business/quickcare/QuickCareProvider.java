package com.business.quickcare;

public class QuickCareProvider {

    private String name;
    private String location;
    private String rating;
    public QuickCareProvider (String name, String location, String rating)
    {
        this.name = name;
        this.location = location;
        this.rating = rating;

    }
    public String getName()
    {
        return this.name;
    }
    public String getLocation()
    {
        return this.location;
    }
    public String getRating()
    {
        return this.rating;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setLocation(String location)
    {
        this.location = location;
    }
    public void setRating(String rating)
    {
        this.rating = rating;
    }




}
