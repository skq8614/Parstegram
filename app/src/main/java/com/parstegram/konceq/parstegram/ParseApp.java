package com.parstegram.konceq.parstegram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application{

    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.appId))
                .clientKey(getString(R.string.clientKey))
                .server(getString(R.string.server))
                .build();

        Parse.initialize(configuration);
    }
}
