package com.example.weather.app;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pasha on 2/7/16.
 */
public class SearchFragment extends Fragment {

    public static final String SEARCH_TAG = "SearchFragment";
    public EditText mEnterCity;
    public Button mButtonSearch, mGetLocation;
    public TextView textViewDescription;

    OnClickSearchListener mOnClickSearchListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnClickSearchListener = (OnClickSearchListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement OnClickSearchListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mEnterCity = (EditText) view.findViewById(R.id.etCity);
        mButtonSearch = (Button) view.findViewById(R.id.button_search);
        textViewDescription = (TextView) view.findViewById(R.id.tvDescription);
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CityName", mEnterCity.getText().toString());
                String s = mEnterCity.getText().toString();
                mOnClickSearchListener.cityName(s);
            }
        });

        mGetLocation = (Button) view.findViewById(R.id.button_location);
        mGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Location", mEnterCity.getText().toString());
                String s = mEnterCity.getText().toString();
                mOnClickSearchListener.getLocation(s);
            }
        });

        return view;
    }

    interface OnClickSearchListener {
        void cityName(String city);
        void getLocation(String city);
    }

}
