package com.ucd.user.weatherfitness;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ucd.user.weatherfitness.FetchWeatherTask;
import com.ucd.user.weatherfitness.ListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ScheduleActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    //private ArrayList<String> times = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> times;
    Context context;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Date date = new Date(); // given date
        java.util.Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date); // assigns calendar to given date
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.ENGLISH);
        final String strDate = sdf.format(cal.getTime());
        times=new ArrayList<HashMap<String,String>>();

        ListView listView=(ListView)findViewById(R.id.listview1);
        context=this;
        lv=(ListView) findViewById(R.id.listview1);
        // lv.setAdapter(new CustomAdapter(this,times));//prgmImages

        for(int i=0;i<24;i+=3) {
            if (i < 12){
                HashMap<String,String> temp=new HashMap<String, String>();
                temp.put("FIRST_COLUMN", +i +":00AM");
                temp.put("SECOND_COLUMN", "Score1");
                times.add(temp);
                //times.add(""+i + ":00 AM");
            }
            else {
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put("FIRST_COLUMN", +i + ":00PM");
                temp.put("SECOND_COLUMN", "Score1");
                times.add(temp);
                // times.add(""+i + ":00 PM");
            }
        }

        ListView lst=(ListView)findViewById(R.id.listview1);
        FetchWeatherTask weatherTask = new FetchWeatherTask(lst,this);
        //weatherTask.execute();
        try {
            weatherTask.execute("-6","53").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        lst.setSelection(hour);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setToolbar(toolbar);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class ItemList implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewGroup vg = (ViewGroup)view;
            TextView tv = (TextView)vg.findViewById(R.id.time);
            Toast.makeText(ScheduleActivity.this,tv.getText().toString(),Toast.LENGTH_SHORT).show();

        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Schedule Next 10 slots";
//                case 1:
//                    return "Tomorrow";

            }
            return null;
        }
    }
}
