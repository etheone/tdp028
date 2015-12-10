package com.example.emil.socialize;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


/**
 * Created by Emil on 2015-12-10.
 */
public class Event implements Parcelable {

    public static final MyCreator CREATOR = new MyCreator();

    public String title;
    public String description;
    public String address;
    public String starts;
    public String ends;
    public String creator;
    public String attenders;
    public double latitude;
    public double longitude;

    /**
     * This will be used only by the MyCreator
     * @param source
     */
    public Event(Parcel source) {
        /*
         * Reconstruct from the parcel
         */
        Log.d("Item", "Item(Parcel source): time to put back parcel data");

        title = source.readString();
        description = source.readString();
        address = source.readString();
        starts = source.readString();
        ends = source.readString();
        creator = source.readString();
        attenders = source.readString();
        latitude = source.readDouble();
        longitude = source.readDouble();

    }

    public Event(String title, String description, String address, String starts, String ends, String creator, String attenders, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.starts = starts;
        this.ends = ends;
        this.creator = creator;
        this.attenders = attenders;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d("Item", "writeToParcel..." + flags);

        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeString(starts);
        dest.writeString(ends);
        dest.writeString(creator);
        dest.writeString(attenders);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    /**
     * Required class for Parcelable
     */
    public static class MyCreator implements Parcelable.Creator<Event> {
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }
        public Event[] newArray(int size) {
            return new Event[size];
        }
    }
}
