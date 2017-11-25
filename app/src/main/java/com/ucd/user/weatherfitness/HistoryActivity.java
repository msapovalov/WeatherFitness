package com.ucd.user.weatherfitness;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    private ViewPager mViewPager;

    private SectionsPageAdapter mSectionsPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Log.d(TAG, "onCreate:Starting");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabAll(), "All Activities");
        adapter.addFragment(new TabWeek(), "Current Week");
        adapter.addFragment(new TabMonth(),"Current Month");
        viewPager.setAdapter(adapter);

    }
    }
