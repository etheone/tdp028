package com.example.emil.socialize;/**
 * Created by Emil on 2015-11-30.
 */

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class StartApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "OGSmeSdTEx2Z2oRBjDxLvfSV8YcbMTyTW54QC77k", "ZEOm73gD7Milht5R8dhKHPPYxIRsx7cvYNh2I9va");
    }

}
