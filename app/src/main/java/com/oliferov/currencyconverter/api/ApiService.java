package com.oliferov.currencyconverter.api;

import com.oliferov.currencyconverter.pojo.CurrencyResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface ApiService {
    @GET("daily_json.js")
    Observable<CurrencyResponse> getCurrencyResponse();
}
