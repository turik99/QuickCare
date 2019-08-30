package com.business.quickcare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FragmentDRGAdapter extends RecyclerView.Adapter<FragmentDRGAdapter.DRGViewHolder> implements Filterable {


private Context context;
private ArrayList<DRGItem> drgItems;
private ArrayList<DRGItem> drgItemsFull;


    public static class DRGViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case


    private CardView cardView;
    private TextView name;
    private TextView priceSkew;
    private ImageView priceImage;
    private TextView price;


    public View view;

    public DRGViewHolder(View v, CardView cardView, TextView name, TextView priceSkew, ImageView priceImage, TextView price) {
        super(v);
        view = v;
        this.cardView = cardView;
        this.name = name;
        this.priceSkew = priceSkew;
        this.priceImage = priceImage;
        this.price = price;
    }
}

    public FragmentDRGAdapter(Context ctx, ArrayList<DRGItem> drgItems) {
        this.context = ctx;
        this.drgItems = drgItems;
        this.drgItemsFull = new ArrayList<DRGItem>(drgItems);
    }

    @NonNull
    @Override
    public FragmentDRGAdapter.DRGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.procedure_item, parent, false);
        CardView cardView = view.findViewById(R.id.procedureCardView);

        TextView name = view.findViewById(R.id.procedureName);
        TextView priceSkew = view.findViewById(R.id.procedurepricemessage);
        ImageView priceImage = view.findViewById(R.id.priceImage);
        TextView price = view.findViewById(R.id.procedureCost);







        FragmentDRGAdapter.DRGViewHolder drgViewHolder = new FragmentDRGAdapter.DRGViewHolder(view, cardView, name, priceSkew, priceImage, price);
        return drgViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentDRGAdapter.DRGViewHolder holder, int position) {
        holder.name.setText( drgItems.get(position).getName() );
        holder.priceSkew.setText( drgItems.get(position).getCostSkew() );
        holder.price.setText( drgItems.get(position).getPrice() );

    }

    @Override
    public int getItemCount() {
        Log.v("DRGFRAG size", "Size=" + drgItems.size());
        return drgItems.size();
    }

    // set adapter filtered list
    public void setList(ArrayList<DRGItem> list) {
        this.drgItems = list;
    }



    @Override
    public Filter getFilter() {
        return drgFilter;
    }

    private Filter drgFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DRGItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                 filteredList.addAll(drgItemsFull);

            }
            else{
                String filterPattern = constraint.toString().toLowerCase();
                Log.v("filter constraint", filterPattern);

                for (DRGItem item : drgItemsFull) {
                    Log.v("Filter items loop", item.getName());

                    if (item.getName().toLowerCase().trim().contains(filterPattern)){
                        filteredList.add(item);

                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            drgItems.clear();
            drgItems.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

}