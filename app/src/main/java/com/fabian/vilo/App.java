package com.fabian.vilo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Fabian on 10/10/15.
 */
public class App extends Application {

    public static Context context;

    public App()
    {
    }

    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
    }

}
