package com.business.quickcare;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

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
    private GoogleMap map;
    public GeoCoder(String address, GoogleMap googleMap)
    {
        this.address = address;
        map = googleMap;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        address = address.replace(" ", "+");
        String url = apiURL + address + "&key=" + apiKey;
        Log.v("GeoCoder URL test", url);
        try {
            returnData = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONObject jsonObject = new JSONObject(returnData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            jsonObject = jsonArray.getJSONObject(0);
            jsonObject = jsonObject.getJSONObject("geometry");
            jsonObject = jsonObject.getJSONObject("location");
            latLongArray[0] = String.valueOf(jsonObject.get("lat"));
            latLongArray[1] = String.valueOf(jsonObject.get("lng"));


        } catch (Exception e) {
            e.printStackTrace();
        }



        Log.v("GeoCoder", latLongArray[0] + " , " + latLongArray[1]);

        return null;

    }


    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
        LatLng location = new LatLng(Double.valueOf(latLongArray[0]), Double.valueOf(latLongArray[1]));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);


    }
}
