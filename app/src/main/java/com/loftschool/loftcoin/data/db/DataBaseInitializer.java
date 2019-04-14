package com.loftschool.loftcoin.data.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class DataBaseInitializer {

    public void init(Context context) {
        Realm.init(context);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("loftschool.realm")
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
