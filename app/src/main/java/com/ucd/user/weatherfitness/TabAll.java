package com.ucd.user.weatherfitness;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by mihhail_shapovalov on 11/18/17.
 *//

//Class for All activities fragment

public class TabAll extends Fragment {

    private static final String TAG = "TabWeek";
    DBAdapter myDb;
    @Nullable
    @Override
    //we inflate the fragment with view and populate list of workouts
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_all, container, false);
        populateListView(view);
        return view;

    }
    //methods to run on fragment creation
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DBAdapter(getActivity());
        myDb.openread();//open read only as we only need to show records here
    }
    
    //method to populate list from SQLite and using item_layout, custom layout to show records in particular order and size
    private void populateListView(View view) {

        Cursor cursor = myDb.getAllRows();
        String[] fromFieldNames = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATETIME, DBAdapter.KEY_LOCATION, DBAdapter.KEY_SCORE};
        int[] toViewIDs = new int[]{R.id.TextViewID, R.id.TextViewDate, R.id.TextViewLocation, R.id.TextViewScore};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getActivity().getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = view.findViewById(R.id.Listview_fragment1);
        myList.setAdapter(myCursorAdapter);


    }

}
