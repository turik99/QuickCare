package com.business.quickcare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ProviderResultsActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_results);

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
