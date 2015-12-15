package com.example.emil.socialize;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Emil on 2015-12-09.
 */

public class CustomListAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final ArrayList<Event> items;


    public CustomListAdapter(Context context, int resource, ArrayList<Event> items) {

        super(context, resource, items);
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        LayoutInflater vi;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(R.layout.custom_list_item, parent, false);

        Log.i("AdapterInfo", "Trying to set the stuff");
        Event e = getItem(position);

        if (e != null) {
            Log.i("AdapterInfo", "Trying to set the stuff");
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView description = (TextView) v.findViewById(R.id.description);
            TextView address = (TextView) v.findViewById(R.id.adress);
            TextView starts = (TextView) v.findViewById(R.id.starts);
            TextView ends = (TextView) v.findViewById(R.id.ends);
            TextView creator = (TextView) v.findViewById(R.id.creator);
            TextView attenders = (TextView) v.findViewById(R.id.attenders);

            if (title != null) {
                title.setText(items.get(position).title);
            }

            if (description != null) {
                description.setText(items.get(position).description);
            }

            if (address != null) {
                String adr;
                try {
                    adr = items.get(position).address;
                } catch(Exception ee) {
                    adr = "No address availible";
                }
                address.setText(adr);
            }

            if (starts != null) {
                String strt = items.get(position).starts;
                starts.setText(strt);
            }

            if (ends != null) {
                String end = items.get(position).ends;
                ends.setText((end));
            }

            if (creator != null) {
                creator.setText(items.get(position).creator);
            }

            if (attenders != null) {
                attenders.setText(items.get(position).attenders);
            }
        }

        return v;
    }

}

