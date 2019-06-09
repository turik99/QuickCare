package com.business.quickcare;

public class QuickCareProvider {

    private String name;
    private String location;
    private String rating;
    private String id;
    public QuickCareProvider (String name, String location, String rating, String id)
    {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.id = id;

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
    public String getId(){return this.id;}
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
    public void setId(String id){this.id = id;}




}
