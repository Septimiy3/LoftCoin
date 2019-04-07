package com.loftschool.loftcoin.data.db;

import android.content.Context;

import com.loftschool.loftcoin.data.db.room.AppDataBase;
import com.loftschool.loftcoin.data.db.room.DataBaseImplRoom;

import androidx.room.Room;

public class DataBaseInitializer {

    public DataBase init(Context context) {
        AppDataBase appDataBase = Room.databaseBuilder(context, AppDataBase.class, "LoftCoin.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();


        return new DataBaseImplRoom(appDataBase);
    }
}
