package com.oliferov.currencyconverter.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.oliferov.currencyconverter.pojo.Currency;
import com.oliferov.currencyconverter.pojo.CurrencyResponse;

@Database(entities = {Currency.class, CurrencyResponse.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "currencies.db";
    private static AppDatabase database;

    private static final Object LOCK = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null) {
                database = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
            return database;
        }
    }

    public abstract CurrencyDao currencyDao();
}
