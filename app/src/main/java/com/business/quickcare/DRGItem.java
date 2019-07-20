package com.business.quickcare;

public class DRGItem {
    private int drgCode;
    private String name;
    private int price;
    private int costSkew;

    public DRGItem(int drgCode, String name, int price, int costSkew)
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
    public int getPrice() {
        return price;
    }
    public int getCostSkew(){
        return costSkew;
    }



}
