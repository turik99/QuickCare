package com.business.quickcare;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.util.ArrayList;

public class ProviderResultsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    int radius = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);



        findProviders();
    }

    public void findProviders()
    {
        String[] coordinates = new String[2];
        coordinates = getIntent().getStringArrayExtra("coordinates");



        double lat = Double.valueOf(coordinates[0]);
        double lng = Double.valueOf(coordinates[1]);



        Log.v("IntentExtra", coordinates[0] + coordinates[1]);


        //This is instantiating an arraylist of the quickareprovider class, see that class for details on what data is stored here.


        final ArrayList<QuickCareProvider> listOfProviders = new ArrayList<>();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("healthcareproviders");


        GeoFirestore geoFirestore = new GeoFirestore(reference);


        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(lat, lng), radius);

        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot doc, GeoPoint geoPoint) {
                Log.v("GeoFire", "Key Entered");
                Log.v("GeoFire", doc.getId() + geoPoint.toString());

                listOfProviders.add(new QuickCareProvider(doc.getString("name"), doc.getString("address"), doc.getString("rating"), doc.getId(), geoPoint));
            }

            @Override
            public void onDocumentExited(DocumentSnapshot documentSnapshot) {

            }

            @Override
            public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {

            }

            @Override
            public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {

            }

            @Override
            public void onGeoQueryReady() {

                Log.v("GeoFire", "Query Ready");

                if (listOfProviders.size()==0)
                {
                    radius++;
                    findProviders();
                }
                else
                {
                    //Instantiating a recyclerview, which is really a list view. The following code is mostly
                    //boiler plate from the Android Developers website.
                    RecyclerView providerList = findViewById(R.id.providerList);

                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    providerList.setHasFixedSize(true);

                    // use a linear layout manager
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    providerList.setLayoutManager(layoutManager);

                    // specify an adapter (see also next example)
                    mAdapter = new MyAdapter(listOfProviders, ProviderResultsActivity.this);
                    providerList.setAdapter(mAdapter);
                }

            }

            @Override
            public void onGeoQueryError(Exception e) {

            }
        });
    }

}
