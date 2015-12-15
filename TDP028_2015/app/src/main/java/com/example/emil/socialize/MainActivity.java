package com.example.emil.socialize;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.maps.model.LatLng;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


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
        if (savedInstanceState == null) {
            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getFragmentManager().findFragmentById(R.id.navigation_drawer);
            mTitle = "Home";

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));


            homeFragment = new HomeFragment();
            listFragment = new ActivitiesFragmentList();
            mapFragment = new MapsFragment();


            FragmentManager fragmentManager = getFragmentManager();

            try {
                if (fragmentManager != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .commit();
                }
            } catch (Exception e) {

                e.printStackTrace();

            }
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<Event> temp = new ArrayList<Event>();
                    for(ParseObject object : objects){
                        String eventId = object.getObjectId();
                        String title = object.getString("title");
                        String description = object.getString("description");
                        String address = object.getString("address");
                        String starts = object.getString("startTime") + " " + object.getString("startDate");
                        String ends = object.getString("endTime") + " " + object.getString("endDate");
                        String creator = object.getString("creator");
                        String attenders = object.get("attenders").toString() + "/" + object.get("maxAttenders").toString();
                        Double latitude = object.getParseGeoPoint("geopoint").getLatitude();
                        Double longitude = object.getParseGeoPoint("geopoint").getLongitude();
                        ArrayList<String> attendingUsers = new ArrayList<String>();
                        attendingUsers = (ArrayList<String>)object.get("attendingUsers");
                        String users = "";
                        for(int i = 0; i < attendingUsers.size(); i++) {
                            users += (attendingUsers.get(i));
                            users += ", ";
                        }
                        Event event = new Event(eventId, title, description, address, starts, ends, creator, attenders, latitude, longitude, users);
                        temp.add(event);
                        Log.i("DBINFO", "Finished fetching db stuff");
                    }
                    events = temp;

                } else {
                    e.printStackTrace();
                }
            }
        });

    }

   @Override
    protected void onResume() {
       Log.i("INFO", "is being called");
       ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
       query.findInBackground(new FindCallback<ParseObject>() {
           @Override
           public void done(List<ParseObject> objects, ParseException e) {
               if (e == null) {
                   ArrayList<Event> temp = new ArrayList<Event>();
                   for (ParseObject object : objects) {
                       String eventId = object.getObjectId();
                       String title = object.getString("title");
                       String description = object.getString("description");
                       String address = object.getString("address");
                       String starts = object.getString("startTime") + " " + object.getString("startDate");
                       String ends = object.getString("endTime") + " " + object.getString("endDate");
                       String creator = object.getString("creator");
                       String attenders = object.get("attenders").toString() + "/" + object.get("maxAttenders").toString();
                       Double latitude = object.getParseGeoPoint("geopoint").getLatitude();
                       Double longitude = object.getParseGeoPoint("geopoint").getLongitude();
                       ArrayList<String> attendingUsers = new ArrayList<String>();
                       attendingUsers = (ArrayList<String>) object.get("attendingUsers");
                       String users = "";
                       for (int i = 0; i < attendingUsers.size(); i++) {
                           users += (attendingUsers.get(i));
                           users += ", ";
                       }
                       Event event = new Event(eventId, title, description, address, starts, ends, creator, attenders, latitude, longitude, users);
                       temp.add(event);
                       Log.i("DBINFO", "Finished fetching db stuff");
                   }
                   events = temp;

               } else {
                   e.printStackTrace();
               }
           }
       });
        super.onResume();
    }

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
                break;
            case 1:
                if(mapsView) {
                    selectedMapFragment = mapFragment;
                    map = true;

                } else {
                    selectedFragment = listFragment;

                }
                break;
            case 2:
                ParseUser.logOut();
                Intent i = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(i);
                break;

        }

        if(selectedFragment != null) {

            if (selectedFragment.isVisible()) {
                return;
            } else {
                fragmentSwapper(selectedFragment);
                onSectionAttached(position + 1);
            }
        } else {

            if(map) {
                if (selectedMapFragment.isVisible()) {
                    return;
                } else {
                    swapToMapFragment(selectedMapFragment);
                    onSectionAttached(position + 1);
                }

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


                if(mapsView == true) {
                    fragmentSwapper(listFragment);

                }

                    //changeMenu(true);

                return true;
            case R.id.showMap:


                if(mapsView == false) {
                    swapToMapFragment(mapFragment);

                } else {

                }
                    //changeMenu(true);


                return true;
        }

        if (item.getItemId() == R.id.action_create) {
            Intent i = new Intent(getApplicationContext(), NewEventActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setCenterPos(LatLng centerPos) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void fragmentSwapper(Fragment selectedFragment) {
        FragmentManager fragmentManager = getFragmentManager();
        try {
            Bundle data = new Bundle();
            data.putParcelableArrayList("events", events);
            selectedFragment.setArguments(data);
            Log.i("TAAG", selectedFragment.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

            if(mapsView == true) {
                hideMapFragment();
            }

        mapsView = false;
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
        try {
            Bundle data = new Bundle();
            data.putParcelableArrayList("events", events);
            selectedFragment.setArguments(data);
        } catch(Exception e) {
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if(mapsView == false) {
            hideFragment();
        }

        mapsView = true;
        fragmentManager.beginTransaction()
                .show(mapFragment)
                .replace(R.id.container, selectedFragment)
                .commit();

    }

}
