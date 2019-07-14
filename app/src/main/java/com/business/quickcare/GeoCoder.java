package com.business.quickcare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
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
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private Button getDirectionsButton;
    private Context ctx;
    public GeoCoder(Context context, String address, GoogleMap googleMap, Button button, FirebaseFirestore db, DocumentReference docref)
    {
        this.db = db;
        this.documentReference = docref;
        this.ctx = context;
        this.address = address;
        map = googleMap;
        this.getDirectionsButton = button;
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
        final LatLng location = new LatLng(Double.valueOf(latLongArray[0]), Double.valueOf(latLongArray[1]));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);


        getDirectionsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("geo:" + location.latitude + "," + location.longitude);

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Attempt to start an activity that can handle the Intent

                ctx.startActivity(mapIntent);

            }
        });


        GeoFirestore geoFirestore = new GeoFirestore(db.collection("healthcareproviders"));
        geoFirestore.setLocation(documentReference.getId(), new GeoPoint(Double.valueOf(latLongArray[0]), Double.valueOf(latLongArray[1])));


    }
}
