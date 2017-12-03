package com.ucd.user.weatherfitness;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by mihhail_shapovalov on 11/18/17.
 */

public class TabWeek extends Fragment {
    private static final String TAG = "TabWeek";

    DBAdapter myDb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_week, container, false);
        populateListView(view);
        return view;

    }

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DBAdapter(getActivity());
        //open activity before db connection
        myDb.open();
    }

    private void populateListView(View view) {
        Calendar cal = Calendar.getInstance();
        //Convert date to simpleformat
        cal.add(Calendar.DAY_OF_YEAR, -7);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String sevenDaysAgo =format1.format(cal.getTime());
        Log.d("Tabweek", sevenDaysAgo);

        Cursor cursor = myDb.getRow(sevenDaysAgo);
        String[] fromFieldNames = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATETIME, DBAdapter.KEY_LOCATION, DBAdapter.KEY_SCORE};
        int[] toViewIDs = new int[]{R.id.TextViewID, R.id.TextViewDate, R.id.TextViewLocation, R.id.TextViewScore};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = view.findViewById(R.id.Listview_week);
        myList.setAdapter(myCursorAdapter);


    }

}