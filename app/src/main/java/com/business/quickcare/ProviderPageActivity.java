package com.business.quickcare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class ProviderPageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String documentId;
    GoogleMap map;
    MapView mapView;
    FirebaseFirestore db;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_page);
//        Toolbar toolbar = findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);

        this.documentId = getIntent().getStringExtra("documentId");


        final TextView providerDetailsName = findViewById(R.id.providerDetailsName);
        final TextView providerDetailsAddress = findViewById(R.id.providerDetailsAddress);
        final TextView practiceSummaryDetails = findViewById(R.id.practiceSummaryDetails);
        final TextView ratingDetailsText = findViewById(R.id.ratingDetailsText);
        final TextView pricingSummary = findViewById(R.id.priceSummary);
        final Button getDirButton = findViewById(R.id.getDirectionsButton);



        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        searchView = findViewById(R.id.searchProcedures);

        //This keeps the keyboard from showing up when that searchbar is set to 'open'
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);





//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("healthcareproviders").document(documentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        toolbar.setTitle(document.getString("name"));
//                        toolbar.setTitle("");
                        Log.d("HP Page Firebase", "DocumentSnapshot data: " + document.getData());
                        providerDetailsName.setText(document.getString("name"));
                        ratingDetailsText.setText(document.getString("rating"));
                        String address = document.getString("address");
                        providerDetailsAddress.setText(address);
                        practiceSummaryDetails.setText(document.getString("summary"));
                        int priceskew = Integer.valueOf(document.getString("priceskew"));

                        switch (priceskew)
                        {
                            case 0:


                        }



                        mapView.onStart();
                        GeoCoder geoCoder = new GeoCoder(getApplicationContext(), address, map, getDirButton, db, docRef);
                        geoCoder.execute();




                    } else {
                        Log.d("HP Page Firebase", "No such document");
                    }
                } else {
                    Log.d("HP Page Firebase", "get failed with ", task.getException());
                }
            }
        });



        CollectionReference prices = docRef.collection("prices");
        prices.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //                        HashMap<String, Object> drgMap = (HashMap<String, Object>) document.get("pricelist");
                ArrayList<DRGItem> drgItems = new ArrayList<>();
                RecyclerView recyclerView = findViewById(R.id.priceListDetails);
                recyclerView.setNestedScrollingEnabled(false);

                for (DocumentSnapshot doc: task.getResult().getDocuments())
                {
                    drgItems.add(new DRGItem(doc.getString("DRG"),
                            doc.getString("name"),
                            doc.getString("price"),
                            String.valueOf(doc.get("priceskew"))));
                }
                recyclerView.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);

                DRGItemAdapter drgItemAdapter = new DRGItemAdapter(ProviderPageActivity.this, drgItems);
                recyclerView.setAdapter(drgItemAdapter);


            }
        });








    }
    public void searchViewClicked(View view)
    {
        searchView.setIconified(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }


}
