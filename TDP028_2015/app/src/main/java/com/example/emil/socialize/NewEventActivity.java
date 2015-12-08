package com.example.emil.socialize;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class NewEventActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText dateStart;
    EditText dateEnd;
    EditText timeStart;
    EditText timeEnd;
    EditText title;
    EditText location;
    EditText description;
    EditText attendants;
    Place placeToSave;

    private int year, month, day;
    EditText currentField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        dateStart = (EditText)findViewById(R.id.dateStart);
        dateEnd = (EditText)findViewById(R.id.dateEnd);
        timeStart = (EditText)findViewById(R.id.timeStart);
        timeEnd = (EditText)findViewById(R.id.timeEnd);
        title = (EditText)findViewById(R.id.title);
        location = (EditText)findViewById(R.id.location);
        description = (EditText)findViewById(R.id.description);
        attendants = (EditText)findViewById(R.id.attendants);

        /////////////////////////////////////////////////////////////////////////
        ////////////////AUTOCOMPLETE GOOGLE PLACES API FUNCTIONS////////////////
        ////////////////////////////////////////////////////////////////////////
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        //setContentView(R.layout.activity_main);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.location);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        //mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
        //mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, BOUNDS_LINKOPING,
                null);
        mAutocompleteView.setAdapter(mAdapter);

        // Set up the 'clear text' button that clears the text in the autocomplete view
        /*Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
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

    @SuppressWarnings("deprecation")
    public void selectDate(View v) {

        switch(v.getId())
        {
            case R.id.dateStart:
                currentField = (EditText)findViewById(R.id.dateStart);
                break;

            case R.id.dateEnd:
                currentField = (EditText)findViewById(R.id.dateEnd);
                break;
        }

        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, 2015, 11, 05);
        }

        if(id == 998) {
            return new TimePickerDialog(this, myTimeListener, 12, 00, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        currentField.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @SuppressWarnings("deprecation")
    public void selectTime(View v) {

        switch(v.getId())
        {
            case R.id.timeStart:
                currentField = (EditText)findViewById(R.id.timeStart);
                break;

            case R.id.timeEnd:
                currentField = (EditText)findViewById(R.id.timeEnd);
                break;
        }

        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();

    }

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

            showTime(arg1, arg2);

        }
    };

    private void showTime(int arg1, int arg2) {

        currentField.setText(new StringBuilder().append(arg1).append(":").append(arg2));

    }

    private void createEvent() {



    }





    /////////////////////////////////////////////////////////////////////////
    ////////////////AUTOCOMPLETE GOOGLE PLACES API FUNCTIONS////////////////
    ////////////////////////////////////////////////////////////////////////
    public static final String TAG = NewEventActivity.class.getSimpleName();
    protected GoogleApiClient mGoogleApiClient;

    private PlaceAutoCompleteAdapter mAdapter;

    private AutoCompleteTextView mAutocompleteView;

    private TextView mPlaceDetailsText;
    private String placeDetailsText;

    private TextView mPlaceDetailsAttribution;

    private static final LatLngBounds BOUNDS_LINKOPING = new LatLngBounds(
            new LatLng(58.410807, 15.621372), new LatLng(58.587745, 16.192420));

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            placeToSave = place;
            Log.i("AppInfo", place.getName() + place.getId() + place.getAddress() + place.getPhoneNumber() + place.getWebsiteUri() + " LATLNG: " + place.getLatLng());
            placeDetailsText = place.getName() + place.getId() + place.getAddress() + place.getPhoneNumber() + place.getWebsiteUri();
            // Format details of the place for display and show it in a TextView.
            /*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));*/

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                //mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                //mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                //mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

            Log.i(TAG, placeDetailsText);

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        //Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
               // websiteUri));

       // return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
         //       websiteUri));
        return null;

    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}
