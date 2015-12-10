package com.example.emil.socialize;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends FragmentActivity
        implements MapsFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, ActivitiesFragmentList.OnFragmentInteractionListener, NavigationDrawerFragment.NavigationDrawerCallbacks {


    //private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HomeFragment homeFragment;
    private ActivitiesFragmentList listFragment = new ActivitiesFragmentList();
    private MapsFragment mapFragment;
    private ArrayList<Event> events = null;

    Menu menu;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean mapsView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = "Home";

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            listFragment = new ActivitiesFragmentList();
            mapFragment = new MapsFragment();

        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<Event> temp = new ArrayList<Event>();
                    for(ParseObject object : objects){
                        String title = object.getString("title");
                        String description = object.getString("description");
                        String address = object.getString("address");
                        String starts = object.getString("startTime") + " " + object.getString("startDate");
                        String ends = object.getString("endTime") + " " + object.getString("endDate");
                        String creator = object.getString("creator");
                        String attenders = object.get("attenders").toString() + "/" + object.get("maxAttenders").toString();
                        Double latitude = object.getParseGeoPoint("geopoint").getLatitude();
                        Double longitude = object.getParseGeoPoint("geopoint").getLongitude();
                        Event event = new Event(title, description, address, starts, ends, creator, attenders, latitude, longitude);
                        temp.add(event);
                        Log.i("DBINFO", "Finished fetching db stuff");
                    }
                        events = temp;

                    } else {
                        e.printStackTrace();
                    }
                }
            });


                FragmentManager fragmentManager = getFragmentManager();


        if(fragmentManager != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, homeFragment)
                    .commit();
        }



      /*  if(mapsview == true) {

            setUpMapIfNeeded();

        }*/

    }



/*    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
  /*  private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
*/
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
   /* private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }*/

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment selectedFragment = null;
        MapsFragment selectedMapFragment = null;
        Boolean map = false;
        Log.i("AppInfo", String.valueOf(position));
        switch(position) {

            case 0:
                mapsView = false;
                selectedFragment = homeFragment;
                //changeMenu(false);
                break;
            case 1:
                if(mapsView) {
                    selectedMapFragment = mapFragment;
                    //changeMenu(true);
                    map = true;

                } else {
                    selectedFragment = listFragment;
                    //changeMenu(true);

                }
                break;
            case 2:
                ParseUser.logOut();
                Intent i = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(i);
                break;

        }
        if(selectedFragment != null) {
          /*  FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, selectedFragment)
                    .commit(); */
            fragmentSwapper(selectedFragment);
            onSectionAttached(position + 1);
        } else {

            if(map) {

                swapToMapFragment(selectedMapFragment);
                onSectionAttached(position + 1);

            }

        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void changeMenu(boolean showMenu){
        if(menu == null)
            return;
        menu.setGroupVisible(R.id.main_menu_group, showMenu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.setGroupVisible(R.id.main_menu_group, false);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.showList:

                mapsView = false;
                fragmentSwapper(listFragment);
                    //changeMenu(true);

                return true;
            case R.id.showMap:


                mapsView = true;
                swapToMapFragment(mapFragment);
                    //changeMenu(true);


                return true;
        }

        if (item.getItemId() == R.id.action_create) {
            Intent i = new Intent(getApplicationContext(), NewEventActivity.class);
            startActivity(i);
           /* Toast.makeText(this, "Create new activity clicked", Toast.LENGTH_SHORT).show();
            return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCenterPos(LatLng centerPos) {
        //this.centerPos = centerPos;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void fragmentSwapper(Fragment selectedFragment) {

        Bundle data = new Bundle();
        data.putParcelableArrayList("events", events);
        selectedFragment.setArguments(data);
        Log.i("TAAG", selectedFragment.toString());


        FragmentManager fragmentManager = getFragmentManager();
        hideMapFragment();
        fragmentManager.beginTransaction()
                .show(selectedFragment)
                .replace(R.id.container, selectedFragment)
                .commit();

    }

    public void hideFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .hide(listFragment)
                .hide(homeFragment)
                .commit();


    }

    public void hideMapFragment() {

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .hide(mapFragment)
                .commit();

    }

    public void swapToMapFragment(MapsFragment selectedFragment) {

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        hideFragment();
        fragmentManager.beginTransaction()
                .show(mapFragment)
                .replace(R.id.container, selectedFragment)
                .commit();

    }

    //////////////////////////////////////////////////////////////////////
    //////////////////////Fill the activities fragment list///////////////
    //////////////////////////////////////////////////////////////////////


}
