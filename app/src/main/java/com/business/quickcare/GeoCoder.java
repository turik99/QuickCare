package com.business.quickcare;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class GeoCoder extends AsyncTask<String, String, String> {
    private String address;
    private String apiURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private String apiKey = "AIzaSyA9Umf_Zl8CVsC51qtzwdtOyuu62PmgBUo";
    private String returnData = "notfilled";
    private String[] latLongArray = new String[2];
    private MapView mapView;
    public GeoCoder(String address, MapView mapView)
    {
        this.address = address;
        this.mapView = mapView;
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
            JSONObject jsonObject = new JSONObject(returnData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("geometry");
            jsonObject = jsonObject.getJSONObject("location");
            latLongArray[0] = String.valueOf(jsonObject.get("lat"));
            latLongArray[1] = String.valueOf(jsonObject.get("long"));
            
        } catch (Exception e) {
            e.printStackTrace();
        }



        Log.v("GeoCoder", latLongArray[0] + " , " + latLongArray[1]);

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
