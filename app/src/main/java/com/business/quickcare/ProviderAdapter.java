package com.business.quickcare;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.MyViewHolder> {

    private ArrayList<QuickCareProvider> providerDataSet;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        CardView cardView;
        TextView providerName;
        TextView location;
        ImageView priceImage;
        View view;

        MyViewHolder(View v, CardView cardView1, TextView providerName1, TextView location1, ImageView priceImage1) {
            super(v);
            view = v;
            cardView = cardView1;
            providerName = providerName1;
            location = location1;
            priceImage = priceImage1;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProviderAdapter(ArrayList<QuickCareProvider> providerList, Context ctx) {
        this.context = ctx;
        providerDataSet = providerList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ProviderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.provider_item, parent, false);

        CardView cardview = v.findViewById(R.id.providerCardView);
        TextView providerName = v.findViewById(R.id.providerName);
        TextView location = v.findViewById(R.id.location);
        ImageView priceImage = v.findViewById(R.id.priceImage);
        MyViewHolder vh = new MyViewHolder(v, cardview, providerName, location, priceImage);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Log.v("onBindViewHolder Test", providerDataSet.get(position).getName());
        holder.providerName.setText(providerDataSet.get(position).getName());
        holder.location.setText(providerDataSet.get(position).getLocation());
        int priceSkew = providerDataSet.get(position).getPrice();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProviderPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("documentId", providerDataSet.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.v("adapter test", "size " + providerDataSet.size());
        return providerDataSet.size();
    }


}
