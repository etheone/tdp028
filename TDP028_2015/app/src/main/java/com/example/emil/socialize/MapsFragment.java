package com.example.emil.socialize;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends SupportMapFragment implements
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback,
        GoogleMap.OnCameraChangeListener {

    private GoogleMap mMap;
    private ArrayList<Event> events = new ArrayList<Event>();
    private Map<Marker, Event> markerEventMap = new HashMap<Marker, Event>();

    private OnFragmentInteractionListener mListener;

    public static MapsFragment newInstance() {

       return new MapsFragment();

    }

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Activity is created for the first time
        } else {

        }

        Toast.makeText(getActivity(), "MapsFragment:onCreate", Toast.LENGTH_SHORT).show();

        getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle extras = getArguments();
        ArrayList<Event> parcedEvents = extras.getParcelableArrayList("events");
        this.events = parcedEvents;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set up mMap to handle different events
        setUpmMap();
        LatLng coord;
        for(Event event : events) {

            coord = new LatLng(event.latitude, event.longitude);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(event.title)
                    .snippet(event.starts + " " + event.description));

            markerEventMap.put(marker, event);
           /* mMap.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(event.title)
                    .snippet(event.starts + " " + event.description)); */

        }

        GPSTracker gps = new GPSTracker(getContext());
        // Add markers to map
        /*LatLng coord;

        for (Item item : markers) {
            coord = new LatLng(item.latitude, item.longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(coord)
                    .title(item.category)
                    .snippet(item.author + " " + item.description));
        }*/


        // TODO Get user position with the class GPSTracker
        // Move the camera
        LatLng cameraPos = new LatLng(gps.getLatitude(), gps.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.zoomTo(8));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPos));

    }


    private void setUpmMap() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (mListener != null) {
                    // Send over the center position of the map
                   mListener.setCenterPos(cameraPosition.target);
                }
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(getActivity(), "Clicked on info marker, id: " + marker.getId() , Toast.LENGTH_LONG).show();
                Event event = markerEventMap.get(marker);

                Intent intent = new Intent();
                intent.setClass(getActivity(), EventInfo.class);
                intent.putExtra("event", event);
                startActivity(intent);
              
            }
        });

    }

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }*/

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment tot allow an interaction in this fragment to be communicated
     * to the activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void setCenterPos(LatLng centerPos);
        public void onFragmentInteraction(Uri uri);
    }


}
