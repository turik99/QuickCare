package com.business.quickcare;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<QuickCareProvider> providerDataSet;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case


        public CardView cardView;
        public TextView providerName;
        public TextView ratingText;
        public TextView location;

        public View view;

        public MyViewHolder(View v, CardView cardView1, TextView providerName1, TextView ratingText1, TextView location1) {
            super(v);
            view = v;
            cardView = cardView1;
            providerName = providerName1;
            ratingText = ratingText1;
            location = location1;

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<QuickCareProvider> providerList, Context ctx) {
        this.context = ctx;
        providerDataSet = providerList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.provider_item, parent, false);

        CardView cardview = v.findViewById(R.id.providerCardView);
        TextView providerName = v.findViewById(R.id.providerName);
        TextView rating = v.findViewById(R.id.rating);
        TextView location = v.findViewById(R.id.location);

        MyViewHolder vh = new MyViewHolder(v, cardview, providerName, rating, location);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Log.v("onBindViewHolder Test", providerDataSet.get(position).getName());
        holder.providerName.setText(providerDataSet.get(position).getName());
        holder.ratingText.setText(providerDataSet.get(position).getRating());
        holder.location.setText(providerDataSet.get(position).getLocation());


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
        return providerDataSet.size();
    }


}
