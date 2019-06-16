package com.business.quickcare;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;

public class GeoCoder extends AsyncTask<String, String, String> {
    private String address;
    private String apiURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String apiKey = "AIzaSyA9Umf_Zl8CVsC51qtzwdtOyuu62PmgBUo";
    private String returnData = "notfilled";

    public GeoCoder(String address)
    {
        this.address = address;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = apiURL + address + "&key=" + apiKey;
        try {
            returnData = Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("GeoCoder", returnData);

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
