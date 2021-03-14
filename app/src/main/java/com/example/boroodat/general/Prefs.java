package com.example.boroodat.general;


import android.content.Context;
import android.content.SharedPreferences;

public class Prefs
{

    private static final String FIRST_LOAD = "firstLoad";
    private static final String PREFS_NAME = "prefs";
    private static Prefs instance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context)
    {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context)
    {

        if (instance == null)
        {
            instance = new Prefs(context);
        }
        return instance;
    }

    public void firstLoadIsDone()
    {

        sharedPreferences
                .edit()
                .putBoolean(FIRST_LOAD, false)
                .apply();
    }

    public boolean isFirstLoad()
    {
        return sharedPreferences.getBoolean(FIRST_LOAD, true);
    }

}
