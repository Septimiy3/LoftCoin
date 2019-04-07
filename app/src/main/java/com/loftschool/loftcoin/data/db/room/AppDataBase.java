package com.loftschool.loftcoin.data.db.room;

import com.loftschool.loftcoin.data.db.model.CoinEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CoinEntity.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract CoinDao coinDao();

}
