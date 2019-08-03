package com.business.quickcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DRGItemAdapter extends RecyclerView.Adapter<DRGItemAdapter.DRGViewHolder> {


    private Context context;
    private ArrayList<DRGItem> drgItems;


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


    public DRGItemAdapter(Context ctx, ArrayList<DRGItem> drgItems) {
        this.context = ctx;
        this.drgItems = drgItems;
    }

    @NonNull
    @Override
    public DRGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.procedure_item, parent, false);
        CardView cardView = view.findViewById(R.id.procedureCardView);

        TextView name = view.findViewById(R.id.procedureName);
        TextView priceSkew = view.findViewById(R.id.procedurepricemessage);
        ImageView priceImage = view.findViewById(R.id.priceImage);
        TextView price = view.findViewById(R.id.procedureCost);


        DRGViewHolder drgViewHolder = new DRGViewHolder(view, cardView, name, priceSkew, priceImage, price);
        return drgViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DRGViewHolder holder, int position) {
        holder.name.setText( drgItems.get(position).getName() );
        holder.priceSkew.setText( drgItems.get(position).getCostSkew() );
        holder.price.setText( drgItems.get(position).getPrice() );

    }

    @Override
    public int getItemCount() {
        return drgItems.size();
    }





}
