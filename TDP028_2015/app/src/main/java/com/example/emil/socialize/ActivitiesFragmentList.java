package com.example.emil.socialize;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ActivitiesFragmentList extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Event> events = new ArrayList<Event>();
    ListView eventsList;
    TextView textView;
    //List<ParseObject> events = new ArrayList<ParseObject>();


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
        Log.i("TAAG", "we here");
        ArrayList<Event> parcedEvents = extras.getParcelableArrayList("events");
        this.events = parcedEvents;
        Log.i("TAAG", "we here 2");
        CustomListAdapter adapter;
        //eventsList = (ListView)getActivity().findViewById(R.id.eventList);


        eventsList = (ListView)rootView.findViewById(R.id.eventList);
        Log.i("TAAG", "we here 3");
        adapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, events);
        Log.i("TAAG", "we here 4");
        setListAdapter(adapter);






        //return inflater.inflate(R.layout.fragment_activities_fragment_list, container, false);
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
                for(int i = 0; i < attendingUsers.size(); i++) {
                    users += (attendingUsers.get(i));
                    users += ", ";
                }
                Event event = new Event(eventId, title, description, address, starts, ends, creator, attenders, latitude, longitude, users);
                temp.add(event);
                Log.i("DBINFO", "Finished fetching db stuff");
            }
        } catch(Exception e) {

        }


        CustomListAdapter adapter;
        Log.i("TAAG", "we here 3");
        adapter = new CustomListAdapter(getActivity().getApplicationContext(), R.layout.custom_list_item, temp);
        Log.i("TAAG", "we here 4");
        setListAdapter(adapter);

        super.onResume();

    }

    // TODO: Rename method, update argument and hook method into UI event
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

        Log.i("TAAG", "we here 5");



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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
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
