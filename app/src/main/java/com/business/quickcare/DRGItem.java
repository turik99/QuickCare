package com.business.quickcare;

public class DRGItem {
    private String drgCode;
    private String name;
    private String price;
    private String costSkew;

    public DRGItem(String drgCode, String name, String price, String costSkew)
    {
        this.drgCode = drgCode;
        this.name = name;
        this.price = price;
        this.costSkew = costSkew;
    }

    public String getDrgCode() {
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
