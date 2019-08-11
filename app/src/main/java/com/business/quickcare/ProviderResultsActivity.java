package com.business.quickcare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

public class ProviderResultsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private GeoFirestore geoFirestore;
    private GeoQuery geoQuery;
    private Query query;
    private FirebaseFirestore db;
    private CollectionReference reference;
    private String[] coordinates = new String[2];

    int radius = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);


        Button goButton = findViewById(R.id.filterButton);


        Spinner providerSpinner = findViewById(R.id.providerSpinner);
        Spinner insuranceSpinner = findViewById(R.id.insuranceSpinner);



        goButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        coordinates = getIntent().getStringArrayExtra("coordinates");

        findProviders();







    }


    public void filterProviders()
    {
        query = reference.whereArrayContains("insuranceproviders", "Aetna");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                
            }
        });
    }


    public void findProviders()
    {



        double lat = Double.valueOf(coordinates[0]);
        double lng = Double.valueOf(coordinates[1]);



        Log.v("IntentExtra", coordinates[0] + coordinates[1]);


        //This is instantiating an arraylist of the quick care provider class, see that class for details on what data is stored here.


        final ArrayList<QuickCareProvider> listOfProviders = new ArrayList<>();


        db = FirebaseFirestore.getInstance();
        reference = db.collection("healthcareproviders");


        geoFirestore = new GeoFirestore(reference);
        geoQuery = geoFirestore.queryAtLocation(new GeoPoint(lat, lng), radius);

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
