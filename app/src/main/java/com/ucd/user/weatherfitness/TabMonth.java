package com.ucd.user.weatherfitness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by mihhail_shapovalov on 11/18/17.
 */


public class TabMonth extends Fragment {
    private static final String TAG = "TabMonth";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_month, container, false);
         return view;
    }
}