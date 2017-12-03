package com.ucd.user.weatherfitness;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    DBAdapter myDb;
    //shared preferences to find the position of switch
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("switch_preference_deletedb")){
                    if (prefs.getBoolean(key,true)){//of switch is ON, delete all records from table 
                    myDb.deleteAll();
                    //Toast for user stating that records been deleted
                    Toast.makeText(SettingsActivity.this, "All Records in Database deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }
    //on activity create run following methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //open db connection
        myDb = new DBAdapter(this);
        myDb.open();
        //show fragment
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);//listener for switch

    }

}
