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


public class TabAll extends Fragment {
    private static final String TAG = "TabWeek";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,@Nullable
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_all, container, false);
        return view;
    }
}