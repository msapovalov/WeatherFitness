package com.ucd.user.weatherfitness;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by mihhail_shapovalov on 11/18/17.
 */

public class TabWeek extends Fragment {
    private static final String TAG = "TabWeek";
    DBAdapter myDb;
    Time today = new Time(Time.getCurrentTimezone());
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_week, container, false);
           return view;

    }

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DBAdapter(getActivity());
        myDb.open();
    }

    private void populateListView(View view) {
        today.setToNow();
        String timestamp = today.format("%Y-%m-%d %H:%M:%S");
        Cursor cursor = myDb.getRow(timestamp);
        String[] fromFieldNames = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATETIME, DBAdapter.KEY_LOCATION, DBAdapter.KEY_SCORE};
        int[] toViewIDs = new int[]{R.id.TextViewID, R.id.TextViewDate, R.id.TextViewLocation, R.id.TextViewScore};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) view.findViewById(R.id.Listview_week);
        myList.setAdapter(myCursorAdapter);


    }

}