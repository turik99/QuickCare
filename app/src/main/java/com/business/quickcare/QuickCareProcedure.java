package com.business.quickcare;

public class QuickCareProcedure {

    private int drgCode;
    private String name;
    private String priceSkew;

    public QuickCareProcedure(int drgCode, String name, String priceSkew)
    {
        this.drgCode = drgCode;
        this.name = name;
        this.priceSkew = priceSkew;

    }

    public int getDrgCode()
    {
        return drgCode;
    }

    public String getName() {
        return name;
    }

    public String getPriceSkew() {
        return priceSkew;
    }

    public void setDrgCode(int drgCode) {
        this.drgCode = drgCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriceSkew(String priceSkew) {
        this.priceSkew = priceSkew;
    }
}
