package com.business.quickcare;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class SearchBarGeoCoder extends AsyncTask<String, String, String> {
    Location location;
    AutocompleteSupportFragment fragment;
    String data;
    String address;
    SharedPreferences sharedPreferences;
    public SearchBarGeoCoder(Location location, AutocompleteSupportFragment fragment, SharedPreferences sharedPreferences)
    {
        this.sharedPreferences = sharedPreferences;
        this.location = location;
        this.fragment = fragment;
    }


    @Override
    protected String doInBackground(String... strings) {


        try {
            data = Jsoup.connect("https://maps.googleapis.com/maps/api/geocode/json?address="+ location.getLatitude() + "," + location.getLongitude() + "&key=AIzaSyA9Umf_Zl8CVsC51qtzwdtOyuu62PmgBUo").ignoreContentType(true).execute().body();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("results");
            jsonObject = array.getJSONObject(0);
            address = jsonObject.getString("formatted_address");

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", address);
        editor.apply();
        fragment.setText(address);

        super.onPostExecute(s);
    }
}
