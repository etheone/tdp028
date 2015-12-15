package com.example.emil.socialize;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ActivitiesFragmentList extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Event> events = new ArrayList<Event>();
    ListView eventsList;

    public static ActivitiesFragmentList newInstance() {

        return new ActivitiesFragmentList();
    }

    public ActivitiesFragmentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activities_fragment_list, container, false);
        // Inflate the layout for this fragment

        Bundle extras = getArguments();
        ArrayList<Event> parcedEvents = extras.getParcelableArrayList("events");
        this.events = parcedEvents;
        CustomListAdapter adapter;

        //Populates the list
        eventsList = (ListView)rootView.findViewById(R.id.eventList);
        adapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, events);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        ArrayList<Event> temp = new ArrayList<Event>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        try {
            List<ParseObject> objects = query.find();
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
                try {
                    for (int i = 0; i < attendingUsers.size(); i++) {
                        users += (attendingUsers.get(i));
                        users += ", ";
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                Event event = new Event(eventId, title, description, address, starts, ends, creator, attenders, latitude, longitude, users);
                temp.add(event);
                Log.i("DBINFO", "Finished fetching db stuff");
            }
        } catch(Exception e) {

        }

        //Populates the list
        CustomListAdapter adapter;
        adapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, temp);
        setListAdapter(adapter);

        super.onResume();

    }

    //Could be used in the future for more advanced fragmentinteraction (Navdrawer)
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Log.i("AppInfo", "Item in list clicked!");
        Event event = events.get(position);

        Intent intent = new Intent();
        intent.setClass(getActivity(), EventInfo.class);
        intent.putExtra("event", event);
        startActivity(intent);

    }

}
