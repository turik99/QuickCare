package com.business.quickcare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProviderResultsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GeoFirestore geoFirestore;
    private GeoQuery geoQuery;
    private Query query;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String[] coordinates = new String[2];
    private String insuranceChoice;
    private String providerChoice;
    private Spinner providerSpinner;
    private Spinner insuranceSpinner;
    int radius = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);


        Button goButton = findViewById(R.id.filterButton);


        providerSpinner = findViewById(R.id.providerSpinner);
        insuranceSpinner = findViewById(R.id.insuranceSpinner);






        goButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        coordinates = getIntent().getStringArrayExtra("coordinates");

        findProviders();



    }


    public void filterProviders(ArrayList<DocumentSnapshot> snapshots)
    {
        insuranceChoice = insuranceSpinner.getSelectedItem().toString();
        //Don't want to check the document snapshot for the term any insurance because it well never contain this string..
        if (insuranceChoice.contains("Any insurance"))
        {
            insuranceChoice = "";
        }

        for (DocumentSnapshot snapshot: snapshots)
        {
//            if (snapshot.toString().contains(providerSpinner.getSelectedItem().toString()))


        }



    }


    public void findProviders()
    {



        double lat = Double.valueOf(coordinates[0]);
        double lng = Double.valueOf(coordinates[1]);



        Log.v("IntentExtra", coordinates[0] + coordinates[1]);


        //This is instantiating an arraylist of the quick care provider class, see that class for details on what data is stored here.


        final ArrayList<QuickCareProvider> listOfProviders = new ArrayList<>();


        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("healthcareproviders");


        geoFirestore = new GeoFirestore(collectionReference);
        geoQuery = geoFirestore.queryAtLocation(new GeoPoint(lat, lng), radius);

        ArrayList<DocumentSnapshot> documentSnapshots = new ArrayList<>();

        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot doc, GeoPoint geoPoint) {
                Log.v("GeoFire", "Key Entered");
                Log.v("GeoFire", doc.getId() + geoPoint.toString());

                documentSnapshots.add(doc);
                Log.v("GeoFire", doc.toString());

                listOfProviders.add(new QuickCareProvider(doc.getString("name"), doc.getString("address"), doc.getString("rating"), doc.getId(), geoPoint, Integer.valueOf(doc.getString("priceskew"))));
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
