package com.mathedia.clubby;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ClubActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public boolean logout(boolean isClicked) {
        if (isClicked) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Abmelden erfolgreich", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(
                                ClubActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Abmelden fehlgeschlagen", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });
        }
        return true;
    }

    /*
    *
    * ADAPTER FÜR DIE TABS
    *
    */

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // gibt das entsprechende Fragment zurück.
            switch (position) {
                case 0:
                    return DatesFragment.newInstance(position + 1);
                default:
                    return ClubsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Termine";
                case 1:
                    return "Clubs";
            }
            return null;
        }
    }

    /*
    *
    * TERMINE FRAGMENT
    *
    */

    public static class DatesFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DatesFragment newInstance(int sectionNumber) {
            DatesFragment fragment = new DatesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public DatesFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_dates, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if (id == R.id.action_logout) {
                ((ClubActivity)getActivity()).logout(true);
            }
            return super.onOptionsItemSelected(item);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_club, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /*
    *
    * CLUBS FRAGMENT
    *
    */

    public static class ClubsFragment extends ListFragment {

        private static final ArrayList<String> clubIDs = new ArrayList<>();

        // gets all clubs where current user is member
        ArrayAdapter adapter;

        public ClubsFragment() {
        }

        public static ClubsFragment newInstance() {
            ClubsFragment fragment = new ClubsFragment();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        //@Override
        //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //View rootView = inflater.inflate(R.layout.fragment_club, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText("Hier im Clubs fragment");
            //return rootView;
        //}

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            setListAdapter(adapter);

            // Is done in onResume()
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Club");
//            query.whereEqualTo("users", ParseUser.getCurrentUser());
//
//            query.findInBackground(new FindCallback<ParseObject>() {
//                public void done(List<ParseObject> clubList, ParseException e) {
//                    if (e == null) {
//                        Log.d("score", "Retrieved " + clubList.size() + " scores");
//                        for (int i = 0; i < clubList.size(); i++) {
//                            ParseObject club = clubList.get(i);
//                            String clubName = club.getString("clubName");
//                            adapter.add(clubName);
//                            clubIDs.add(club.getObjectId());
//                        }
//                    } else {
//                        Log.d("score", "Error: " + e.getMessage());
//                    }
//                }
//            });
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_clubs, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_logout) {
                ((ClubActivity)getActivity()).logout(true);
            }
            if (id == R.id.action_create_club) {
                Intent intent = new Intent(getActivity(), CreateClubActivity.class);
                startActivityForResult(intent, 0);
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onResume() {
            super.onResume();
            adapter.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Club");
            query.whereEqualTo("users", ParseUser.getCurrentUser());

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> clubList, ParseException e) {
                    if (e == null) {
                        Log.d("score", "Retrieved " + clubList.size() + " scores");
                        for (int i = 0; i < clubList.size(); i++) {
                            ParseObject club = clubList.get(i);
                            String clubName = club.getString("clubName");
                            adapter.add(clubName);
                            clubIDs.add(club.getObjectId());
                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            String selection = " "+position;
            Toast.makeText(getActivity(), selection, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), ClubMembersActivity.class);
            //In extras wird der angeklickte Club gespeichert
            intent.putExtra("clubID", clubIDs.get(position));
            startActivity(intent);
        }
    }
}
