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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoLocation;
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
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(47.82, -81.22), 0.6);


        ArrayList<String[]> documentsList = new ArrayList<String[]>();


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String s, GeoPoint geoPoint) {
                //This is the real 'key' (no pun intended) to the library, on key entered menas that there is a document with a 'key' that's the id inside your Query
                //which is what we want, we want to see those documents that are within the radius of our user.
                //I am not a programmer so I found the logic funny here, but this method does not produce a list of keys that are in the radius, rather it produces one, and then it is called again and again
                //if there is another id that is there.
                String[] strings = new String[3];
                strings[0] = s;
                strings[1] = String.valueOf(geoPoint.getLatitude());
                strings[2] = String.valueOf(geoPoint.getLongitude());

                documentsList.add(strings);
            }

            @Override
            public void onKeyExited(String s) {

            }

            @Override
            public void onKeyMoved(String s, GeoPoint geoPoint) {

            }

            @Override
            public void onGeoQueryReady() {
                for (String[] s: documentsList)
                {
                    Log.v("GeoFire Query", s[0]);

                    double lat = Double.valueOf(s[1]);
                    double lng = Double.valueOf(s[2]);

                    DocumentReference docRef = reference.document(s[0]);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot doc) {
                            //this is called when the document is gotten successfully as it should be yeet
                            listOfProviders.add(new QuickCareProvider(doc.getString("name"),
                                    doc.getString("address"), doc.getString("rating"), doc.getId(), new GeoPoint(lat, lng)));


                        }
                    });
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

            @Override
            public void onGeoQueryError(Exception e) {

            }
        });


        Log.v("ProviderDataSetTest", listOfProviders.toString());





        /*Whoah THere Bucko! This code is only for testing! We're sorting by distance and stuff
        now, so be sure to not have this around anymore soon.
         */


//        FirebaseFirestore dbTest = FirebaseFirestore.getInstance();
//        db.collection("healthcareproviders")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    private String TAG = "FireBaseFireStore";
//
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                listOfProviders.add(new QuickCareProvider(document.getString("name"),
//                                        String.valueOf(document.get("ZipCode")),
//                                        document.getString("rating"), document.getId(), new GeoPoint(-87.7346, 41.7218)));
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//
//
//
//                        //Instantiating a recyclerview, which is really a list view. The following code is mostly
//                        //boiler plate from the Android Developers website.
//                        RecyclerView providerList = findViewById(R.id.providerList);
//
//                        // use this setting to improve performance if you know that changes
//                        // in content do not change the layout size of the RecyclerView
//                        providerList.setHasFixedSize(true);
//
//                        // use a linear layout manager
//                        layoutManager = new LinearLayoutManager(getApplicationContext());
//                        providerList.setLayoutManager(layoutManager);
//
//                        // specify an adapter (see also next example)
//                        mAdapter = new MyAdapter(listOfProviders, ProviderResultsActivity.this);
//                        providerList.setAdapter(mAdapter);
//
//
//
//                    }
//                });
//
//
//        Log.v("ProviderDataSetTest", listOfProviders.toString());


        /*End of testing code!!!!!!!!
        * */


















    }
    public void doStuffWithDocumentIds(ArrayList<String> ids)
    {

    }
}
