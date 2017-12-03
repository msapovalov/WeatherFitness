package com.ucd.user.weatherfitness;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";
    //methods to run on creating activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate:Starting");
        //viewpager adaptor to work with fragments
        ViewPager mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    //setting up fragments
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabAll(), "All Activities");
        adapter.addFragment(new TabWeek(), "Last 7 days");
        adapter.addFragment(new TabMonth(),"Last 30 days");
        viewPager.setAdapter(adapter);

    }
    }
