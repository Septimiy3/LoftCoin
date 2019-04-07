package com.loftschool.loftcoin;

import android.app.Application;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.ApiInitilizer;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.DataBaseInitializer;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.data.prefs.PrefsImpl;

import timber.log.Timber;

public class App extends Application {


    private Prefs prefs;
    private Api api;
    private DataBase dataBase;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        prefs = new PrefsImpl(this);
        api = new ApiInitilizer().init();
        dataBase = new DataBaseInitializer().init(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public Api getApi() {
        return api;
    }

    public DataBase getDataBase() {
        return dataBase;
    }
}
