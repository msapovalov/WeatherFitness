package com.ucd.user.weatherfitness;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_DELETE_DB_SWITCH = "this switch dumps all records";
    DBAdapter myDb;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("switch_preference_deletedb")){
                    if (prefs.getBoolean(key,true)){
                    myDb.deleteAll();}
                    //DeleteDB();
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DBAdapter(this);
        myDb.open();

        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);

        //Boolean switchPref = sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_DELETE_DB_SWITCH, false);
        //Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();


    }

    private void DeleteDB() {
        Cursor cursor = myDb.deleteAll();
        String[] fromFieldNames = new String[]{DBAdapter.KEY_ROWID, DBAdapter.KEY_DATETIME, DBAdapter.KEY_LOCATION, DBAdapter.KEY_SCORE};
        int[] toViewIDs = new int[]{R.id.TextViewID, R.id.TextViewDate, R.id.TextViewLocation, R.id.TextViewScore};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this.getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        }

}