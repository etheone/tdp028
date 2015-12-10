package com.example.emil.socialize;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EventInfo extends AppCompatActivity {

    Button joinButton;
    String user;
    ArrayList<String> attendingUsers = new ArrayList<>();
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        joinButton = (Button)findViewById(R.id.joinButton);

        user = ParseUser.getCurrentUser().getUsername();

        Event event = getIntent().getParcelableExtra("event");

        Log.i("Event", event.toString());

        if (event != null) {
            Log.i("AdapterInfo", "Trying to set the stuff");
            TextView title = (TextView) findViewById(R.id.title);
            TextView description = (TextView) findViewById(R.id.description);
            TextView address = (TextView) findViewById(R.id.adress);
            TextView starts = (TextView) findViewById(R.id.starts);
            TextView ends = (TextView) findViewById(R.id.ends);
            TextView creator = (TextView) findViewById(R.id.creator);
            TextView attenders = (TextView) findViewById(R.id.attenders);
            TextView usersAttending = (TextView) findViewById(R.id.usersAttending);

            eventId = event.eventId;

            if (title != null) {
                title.setText(event.title);
                //title.setText((e.get("title")).toString());
            }

            if (description != null) {
                //description.setText((e.get("description")).toString());
                description.setText(event.description);
            }

            if (address != null) {
                String adr;
                try {
                    adr = event.address;
                    //adr = e.get("address").toString();
                } catch (Exception ee) {
                    adr = "No address availible";
                }
                address.setText(adr);
            }

            if (starts != null) {
                String strt = event.starts;
                starts.setText(strt);
            }

            if (ends != null) {
                String end = event.ends;
                ends.setText((end));
            }

            if (creator != null) {
                creator.setText("Creator: " + event.creator);
            }

            if (attenders != null) {
                //int attending = (int) e.get(position).attenders;
                //int maxAttending = (int) e.get("maxAttenders");
                attenders.setText(event.attenders);
            }

            if(usersAttending != null) {
                String[] temp = event.attendingUsers.split(", ");
                for(String s : temp) {
                    if(s != null) {
                        attendingUsers.add(s);
                    }
                }
                usersAttending.setText(event.attendingUsers);


            }
        }
        if(attendingUsers.contains(user)) {
            Log.i("EventInfo", "User is participating");
            joinButton.setText("Leave event");
        } else {
            joinButton.setText("Join event");
            Log.i("EventInfo", "User is NOT participating");
        }

    }

    public void joinLeaveEvent(View v) {

        if(attendingUsers.contains(user)) {
            //User is already signed up
            Log.i("EventInfo", "User is participating");
            leaveEvent();

        } else {
            //User is not signed up yet
            Log.i("EventInfo", "User is NOT participating");
            joinEvent();
        }

    }

    public void leaveEvent() {

        ParseQuery query = ParseQuery.getQuery("Event");
        query.whereEqualTo("objectId", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> events, ParseException e) {
                ArrayList<String> attenders;
                if (e == null) {
                    Log.d("events", "Retrieved " + events.size() + " events");
                    attenders = (ArrayList<String>)events.get(0).get("attendingUsers");
                    for(int i = 0; i < attenders.size(); i++) {
                        Log.d("events", user);
                        if(attenders.get(i).equals(user)) {
                            Log.d("events", "attenders: " + attenders.get(i).toString());
                            Log.d("events", user);
                            attenders.remove(i);
                        }
                    }
                    Log.d("events", "attenders includes: " + attenders.toString());
                    ParseObject p = events.get(0);
                    p.remove("attendingUsers");
                    p.addAll("attendingUsers", attenders);
                    p.increment("attenders", -1);
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getApplicationContext(), "Left event", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void joinEvent() {

        ParseQuery query = ParseQuery.getQuery("Event");
        query.whereEqualTo("objectId", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> events, ParseException e) {
                ArrayList<String> attenders;
                if (e == null) {
                    Log.d("events", "Retrieved " + events.size() + " events");
                    //Log.d("events", "attenders includes: " + attenders.toString());
                    ParseObject p = events.get(0);
                    p.addUnique("attendingUsers", user);
                    p.increment("attenders");
                    p.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getApplicationContext(), "Joined event", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } else {
                    Log.d("events", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_info, menu);
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

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }




        return super.onOptionsItemSelected(item);
    }
}
