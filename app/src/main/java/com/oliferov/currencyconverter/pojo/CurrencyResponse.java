package com.oliferov.currencyconverter.pojo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "date_currencies")
public class CurrencyResponse {

    @SerializedName("Date")
    @Expose
    @PrimaryKey
    @NonNull
    private String date;
    @SerializedName("PreviousDate")
    @Expose
    @Ignore
    private String previousDate;
    @SerializedName("Valute")
    @Expose
    @Ignore
    private Valute valute;

    public CurrencyResponse(@NonNull String date) {
        this.date = date;
    }

    public CurrencyResponse() {
    }

    @Ignore
    private List<JSONObject> currencies = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public Valute getValute() {
        return valute;
    }

    public void setValute(Valute valute) {
        this.valute = valute;
    }
}
