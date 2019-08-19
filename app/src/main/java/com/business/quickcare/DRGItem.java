package com.business.quickcare;

import androidx.annotation.NonNull;

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

    @NonNull
    public String toString()
    {
        return this.drgCode + "_" + this.name + "_" + this.price + "_" + this.costSkew;
    }


}
