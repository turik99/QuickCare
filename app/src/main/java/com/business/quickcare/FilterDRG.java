package com.business.quickcare;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

class FilterDRG extends Filter {

    private ArrayList<DRGItem> drgItemList;
    private ArrayList<DRGItem> filteredDRGList;
    private FragmentDRGAdapter adapter;

    public FilterDRG(ArrayList<DRGItem> drgItemList, FragmentDRGAdapter adapter) {
        this.adapter = adapter;
        this.drgItemList = drgItemList;
        this.filteredDRGList = new ArrayList();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredDRGList.clear();
        final FilterResults results = new FilterResults();

        //here you need to add proper items do filteredContactList
        for (final DRGItem item : drgItemList) {
            if (item.getName().toLowerCase().trim().contains("pattern")) {
                filteredDRGList.add(item);
            }
        }

        results.values = filteredDRGList;
        results.count = filteredDRGList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setList(filteredDRGList);
        adapter.notifyDataSetChanged();
    }
}