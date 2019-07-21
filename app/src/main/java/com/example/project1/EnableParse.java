package com.example.project1;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class EnableParse extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("b57d4385a55844e05b823b5dbf1716cf1d0950b6")
                .clientKey("f6eb87109ca488ec88827a63cfbf6f4763db5469")
                .server("http://18.188.115.162:80/parse/")
                .build()
        );
        //b57d4385a55844e05b823b5dbf1716cf1d0950b6
        //f6eb87109ca488ec88827a63cfbf6f4763db5469
        //http://18.188.115.162:80/parse
        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
