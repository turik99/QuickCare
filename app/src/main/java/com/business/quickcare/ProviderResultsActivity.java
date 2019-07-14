package com.business.quickcare;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;
import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener;

import java.util.ArrayList;

public class ProviderResultsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);

        double[] coordinates = getIntent().getDoubleArrayExtra("coordinates");


        //Before we starting messing with the UI elements like the recycler view and stuff, we're going to get the Firebase data
        //Remember that we will eventually need to be sorting this data w/ ui elements like the spinners
        //and also sorting the data by location naturally



        //This is instantiating an arraylist of the quickareprovider class, see that class for details on what data is stored here.
        final ArrayList<QuickCareProvider> listOfProviders = new ArrayList<>(10);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("healthcareproviders");






        GeoFirestore geoFirestore = new GeoFirestore(reference);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(coordinates[0], coordinates[1]), 0.6);
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {

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

            }

            @Override
            public void onGeoQueryError(Exception e) {

            }
        });


        db.collection("healthcareproviders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private String TAG = "FireBaseFireStore";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String[] coordinatesArray = new String[2];

                                Log.d(TAG, document.getId() + " => " + document.getData());
                                coordinatesArray[0] = document.getString("lat");
                                coordinatesArray[1] = document.getString("lng");
                                listOfProviders.add(new QuickCareProvider(document.getString("name"),
                                        String.valueOf(document.get("address")),
                                        document.getString("rating"), document.getId(), coordinatesArray));

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }



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
                });






        Log.v("ProviderDataSetTest", listOfProviders.toString());

    }
}
