package com.loftschool.loftcoin;

import android.app.Application;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.ApiInitilizer;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.DataBaseInitializer;
import com.loftschool.loftcoin.data.db.realm.DatabaseImplRealm;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.data.prefs.PrefsImpl;

import timber.log.Timber;

public class App extends Application {


    private Prefs prefs;
    private Api api;


    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        prefs = new PrefsImpl(this);
        api = new ApiInitilizer().init();
        new DataBaseInitializer().init(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public Api getApi() {
        return api;
    }

    public DataBase getDataBase() {
        return new DatabaseImplRealm();
    }
}
