package com.business.quickcare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceListFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    public PriceListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PriceListFragment newInstance() {
        PriceListFragment fragment = new PriceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String[] strings = getArguments().getStringArray("string_arg");

        Log.v("strings", strings.length + " _");

        ArrayList<DRGItem> list = new ArrayList<DRGItem>();

        list = getProcedures(strings);

        FragmentDRGAdapter drgAdapter = new FragmentDRGAdapter(getContext(), list);



        View view = inflater.inflate(R.layout.fragment_price_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragmentPriceRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(drgAdapter);

        SearchView searchView = view.findViewById(R.id.fragmentPriceSearch);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //klas;djfkl;asdjf;l

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                drgAdapter.getFilter().filter(s);

                return false;
            }
        });




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume()
    {

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public ArrayList<DRGItem> getProcedures(String[] dataArray){
        ArrayList<DRGItem> procedures = new ArrayList<>();
        Log.v("stoinrs", dataArray[1]);
        for (String string: dataArray){
            try {
                Log.v("9090", " bruh " +  string);
                String[] itemArray = string.split("_");
                procedures.add(new DRGItem(itemArray[0], itemArray[1], itemArray[2], itemArray[3]));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        return procedures;
    }



}
