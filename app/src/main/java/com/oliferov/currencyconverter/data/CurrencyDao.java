package com.oliferov.currencyconverter.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.oliferov.currencyconverter.pojo.Currency;
import com.oliferov.currencyconverter.pojo.CurrencyResponse;

import java.util.List;

@Dao
public interface CurrencyDao {
    @Query("SELECT * FROM currencies")
    LiveData<List<Currency>> getAllCurrencies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrencies(List<Currency> currencies);

    @Query("DELETE FROM currencies")
    void deleteAllCurrencies();

    @Query("SELECT Date FROM date_currencies")
    LiveData<String> getDate();

    @Insert
    void insertDate(CurrencyResponse data);

    @Query("DELETE FROM date_currencies")
    void deleteDate();
}
