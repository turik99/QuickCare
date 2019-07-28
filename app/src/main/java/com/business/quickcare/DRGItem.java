package com.business.quickcare;

public class DRGItem {
    private int drgCode;
    private String name;
    private String price;
    private String costSkew;

    public DRGItem(int drgCode, String name, String price, String costSkew)
    {
        this.drgCode = drgCode;
        this.name = name;
        this.price = price;
        this.costSkew = costSkew;
    }

    public int getDrgCode() {
        return drgCode;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public String getCostSkew(){
        return costSkew;
    }



}
