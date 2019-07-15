package com.business.quickcare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean permission = false;
    private Location location;
    private FusedLocationProviderClient providerClient;
    private AutocompleteSupportFragment autocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //OK, a bit of context, right now I'm just trying to get something 'off the ground' that will
        //function as intended, so everything written thus far is subjecto change, and the user flow
        //should be more elegant than I'm about to write it.
        //Also research android lifecylce that's the like the only important thing you need to know about
        //android programming.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }

        location = new Location("");


// Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("Places", "Place: " + place.getName() + ", " + place.getId());

                LatLng latLng = place.getLatLng();
                Log.v("lat lng places", place.getLatLng().toString());
                Log.v("lat lng places name", location.getProvider());

                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Places", "An error occurred: " + status);
            }
        });




    }

    public void getLocation(View view)
    {

        int locationRequestCode = 42069;
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permission = false;
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationRequestCode);

        } else {

            permission = true;
            // already permission granted
            providerClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
            providerClient.getLastLocation().addOnSuccessListener(location -> {
                Log.v("providerLocationClient", "got location success");
                SearchBarGeoCoder cder = new SearchBarGeoCoder(location, autocompleteFragment);
                cder.execute();
                MainActivity.this.location = location;
            });
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 42069: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    permission = true;
                    Log.v("Permissions", "granted and success");

                    providerClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                    providerClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.v("providerLocationClient", "got location success");
                            SearchBarGeoCoder cder = new SearchBarGeoCoder(location, autocompleteFragment);
                            cder.execute();
                            MainActivity.this.location = location;


                        }
                    });

                }
                else
                {
                    //The permission was not granted, fuck that
                    //Just do nothing, wait for user to input their address
                    permission = false;
                }
                return;

            }
        }
    }




    public void findProviders(View view)
    {
        //This method is called when the user clicks the find providers button, it's defined in the
        //android:onclick attribute of the xml.

        Intent intent = new Intent(this, ProviderResultsActivity.class);
        String[] strings = new String[2];

        if (location.getLatitude() != 0.0 )
        {

            strings[0] = String.valueOf(location.getLatitude());
            strings[1] = String.valueOf(location.getLongitude());

        }
        else
        {
            Toast.makeText(getBaseContext(), "You didn't provide a location! Defaulting to rural poland.", Toast.LENGTH_LONG).show();
            strings[0] = "49.919";
            strings[1] = "19.527";

        }

        intent.putExtra("coordinates", strings);
        startActivity(intent);

    }





}
