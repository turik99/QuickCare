package com.business.quickcare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProviderResultsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);








        //Before we starting messing with the UI elements like the recycler view and stuff, we're going to get the Firebase data
        //Remember that we will eventually need to be sorting this data w/ ui elements like the spinners
        //and also sorting the data by location naturally

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("healthcareproviders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private String TAG = "FireBaseFireStore";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });









        String[] myDataset = {"Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",
                "Advocate", "Eric Hospitals", "Riley Hospitals", "Dylan Hospitals",};



        //Instantiating a recyclerview, which is really a list view. The following code is mostly
        //boiler plate from the Android Developers website.
        RecyclerView providerList = findViewById(R.id.providerList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        providerList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        providerList.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        providerList.setAdapter(mAdapter);

    }
}
