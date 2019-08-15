package com.business.quickcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProviderPageActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String documentId;
    GoogleMap map;
    MapView mapView;
    FirebaseFirestore db;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private DocumentReference docRef;
    private RecyclerView pricesRecycler;
    TextView providerDetailsName;
    TextView providerDetailsAddress;
    TextView practiceSummaryDetails;
    TextView pricingSummary;
    ImageView priceSkewImage;
    Button getDirButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_page);
//        Toolbar toolbar = findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);

        this.documentId = getIntent().getStringExtra("documentId");

        providerDetailsName = findViewById(R.id.providerDetailsName);
        providerDetailsAddress = findViewById(R.id.providerDetailsAddress);
        practiceSummaryDetails = findViewById(R.id.practiceSummaryDetails);
        pricingSummary = findViewById(R.id.pagepricingsummary);
        getDirButton = findViewById(R.id.getDirectionsButton);
        priceSkewImage = findViewById(R.id.pagePriceImage);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //This keeps the keyboard from showing up when that searchbar is set to 'open'
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getDocumentInfo();


    }




    //Getting the prices and adding them into that recyclerview. its a reference to a collection in the document we already are referencing.

    public void getDocumentInfo()
    {
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("healthcareproviders").document(documentId);
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
                        String address = document.getString("address");
                        providerDetailsAddress.setText(address);
                        practiceSummaryDetails.setText(document.getString("summary"));
                        int priceskew = Integer.valueOf(document.getString("priceskew"));

                        switch (priceskew){
                            case 0:
                                pricingSummary.setText("This provider's services are cheaper than average");
                                priceSkewImage.setImageDrawable(getDrawable(R.drawable.cash_cheap));
                                break;
                            case 1:
                                pricingSummary.setText("This provider's services are about average");
                                priceSkewImage.setImageDrawable(getDrawable(R.drawable.cash_medium));
                                break;
                            case 2:
                                pricingSummary.setText("This provider's services are more expensive than average");
                                priceSkewImage.setImageDrawable(getDrawable(R.drawable.cash_expensive));
                                break;


                        }


                        mapView.onStart();

                        GeoPoint geoPoint = document.getGeoPoint("position");

                        double lat = geoPoint.getLatitude();
                        double lng = geoPoint.getLongitude ();
                        LatLng latLng = new LatLng(lat, lng);

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        map.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
                        map.addMarker(new MarkerOptions().position(latLng));



                        getDirButton.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Create a Uri from an intent string. Use the result to create an Intent.
                                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + latLng.latitude + "," + latLng.longitude + "Hospital");

                                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                // Make the Intent explicit by setting the Google Maps package
                                mapIntent.setPackage("com.google.android.apps.maps");
                                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                // Attempt to start an activity that can handle the Intent


                                getBaseContext().startActivity(mapIntent);

                            }
                        });




                        getPrices();
                    }



                } else {
                    Log.d("Provider Page Firebase", "get failed with ", task.getException());
                }
            }
        });


    }


    public void getPrices()
    {
        CollectionReference prices = docRef.collection("prices");
        prices.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //                        HashMap<String, Object> drgMap = (HashMap<String, Object>) document.get("pricelist");
                ArrayList<DRGItem> drgItems = new ArrayList<>();
                pricesRecycler = findViewById(R.id.priceListDetails);

                for (DocumentSnapshot doc: task.getResult().getDocuments())
                {
                    drgItems.add(new DRGItem(doc.getString("DRG"),
                            doc.getString("name"),
                            doc.getString("price"),
                            String.valueOf(doc.get("priceskew"))));
                }
                pricesRecycler.setHasFixedSize(true);

                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getApplicationContext());
                pricesRecycler.setLayoutManager(layoutManager);
                pricesRecycler.setNestedScrollingEnabled(false);
                pricesRecycler.setLayoutFrozen(true);

                DRGItemAdapter drgItemAdapter = new DRGItemAdapter(ProviderPageActivity.this, drgItems);
                pricesRecycler.setAdapter(drgItemAdapter);


                addStuff();

            }
        });



    }

    public void addStuff()
    {
        Map<String, Object> dataToPut = new HashMap<>();
        dataToPut.put("0", "Aetna");
        dataToPut.put("1", "UnitedHealth");
        dataToPut.put("2", "Allianz Life");

        docRef.update("insuranceproviders", FieldValue.arrayUnion("Aetna", "United Health", "Allianz Life")).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v("added stuff", "wif suksess");
            }
        });
    }

    public void searchViewClicked(View view)
    {
        searchView.setIconified(false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
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
