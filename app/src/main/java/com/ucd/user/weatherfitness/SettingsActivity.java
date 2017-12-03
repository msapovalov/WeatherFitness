package com.ucd.user.weatherfitness;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    DBAdapter myDb;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("switch_preference_deletedb")){
                    if (prefs.getBoolean(key,true)){
                    myDb.deleteAll();
                    Toast.makeText(SettingsActivity.this, "All Records in Database deleted", Toast.LENGTH_SHORT).show();
                    }
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

    }

}