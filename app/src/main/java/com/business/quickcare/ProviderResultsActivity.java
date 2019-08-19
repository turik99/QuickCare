package com.business.quickcare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

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
    private GeoFirestore geoFirestore;
    private GeoQuery geoQuery;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private String[] coordinates = new String[2];
    private String insuranceChoice;
    private String distanceChoice;
    private Spinner insuranceSpinner;
    private Spinner distancesSpinner;
    private ArrayList<QuickCareProvider> finalProviderData;
    int radius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);


        Button goButton = findViewById(R.id.filterButton);
        insuranceSpinner = findViewById(R.id.insuranceSpinner);
        distancesSpinner = findViewById(R.id.distancesSpinner);

        goButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                findProviders();
            }
        });

        coordinates = getIntent().getStringArrayExtra("coordinates");
        findProviders();



    }


    public ArrayList<QuickCareProvider> filterProviders(ArrayList<DocumentSnapshot> snapshots)
    {
        insuranceChoice = insuranceSpinner.getSelectedItem().toString();



        ArrayList<QuickCareProvider> filterProvidersList = new ArrayList<>();
        //Don't want to check the document snapshot for the term any insurance because it well never contain this string..
        if (insuranceChoice.contains("Any "))
        {
            insuranceChoice = "";
        }



        for (DocumentSnapshot snapshot: snapshots)
        {
            String dataString = snapshot.toString();
            //This part checks to see if a string version of the document referenced contains BOTH
            //the name of the provider and the insurance choice.
            Log.v("Filter params", "insurance choice " + insuranceChoice);
            if (dataString.contains(insuranceChoice))
            {
                Log.v("Snapshot Array Filter", "added " + snapshot.getString("name"));
                filterProvidersList.add(new QuickCareProvider(snapshot.getString("name"),
                        snapshot.getString("address"),
                        snapshot.getId(),
                        snapshot.getGeoPoint("l"), Integer.valueOf(snapshot.getString("priceskew"))));

            }
            else
            {
                Log.v("filter test", "snapshot " + snapshot.getString("name") + " filters out" );

            }

        }
        return filterProvidersList;


    }


    public void findProviders()
    {



        double lat = Double.valueOf(coordinates[0]);
        double lng = Double.valueOf(coordinates[1]);



        Log.v("IntentExtra", coordinates[0] + coordinates[1]);


        //This is instantiating an arraylist of the quick care provider class, see that class for details on what data is stored here.




        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("healthcareproviders");


        distanceChoice = distancesSpinner.getSelectedItem().toString();
        int distancemiles = Integer.valueOf(distanceChoice.substring(0, distanceChoice.indexOf(" ")));
        Log.v("I hate programming", distanceChoice.substring(0, distanceChoice.indexOf(" ")));
        radius = (int) milesToKilometers(distancemiles);



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

                if (documentSnapshots.size() == 0)
                {
                    radius++;
                    findProviders();
                }
                else
                {

                    //Use the filter to filter through the list of documentsnaps we acquired by geolocation.
                    finalProviderData = filterProviders(documentSnapshots);
                    Log.v("final providerlist", "size " + finalProviderData.size());
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
                    mAdapter = new ProviderAdapter(finalProviderData, ProviderResultsActivity.this);
                    providerList.setAdapter(mAdapter);
                }

            }

            @Override
            public void onGeoQueryError(Exception e) {

            }
        });
    }

    public double kilometersToMiles(int kilometers)
    {
        return kilometers * 0.621371;
    }
    public double milesToKilometers(int miles)
    {
        return miles * 1.60934;
    }

}
