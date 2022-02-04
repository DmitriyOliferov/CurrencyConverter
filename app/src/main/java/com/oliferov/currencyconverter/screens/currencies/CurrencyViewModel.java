package com.oliferov.currencyconverter.screens.currencies;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oliferov.currencyconverter.R;
import com.oliferov.currencyconverter.api.ApiFactory;
import com.oliferov.currencyconverter.api.ApiService;
import com.oliferov.currencyconverter.autoupdate.AutoUpdate;
import com.oliferov.currencyconverter.converter.ConverterCurrency;
import com.oliferov.currencyconverter.data.AppDatabase;
import com.oliferov.currencyconverter.pojo.Currency;
import com.oliferov.currencyconverter.pojo.CurrencyResponse;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CurrencyViewModel extends AndroidViewModel {

    private static AppDatabase db;
    private LiveData<List<Currency>> currencies;
    private LiveData<String> dateLastUpdate;
    private MutableLiveData<Throwable> errors;
    private CompositeDisposable compositeDisposable;
    private String dateUpdateDB;

    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        currencies = db.currencyDao().getAllCurrencies();
        dateLastUpdate = db.currencyDao().getDate();
        errors = new MutableLiveData<>();
    }

    public LiveData<Throwable> getErrors() {
        return errors;
    }

    public void clearErrors() {
        errors.setValue(null);
    }

    public LiveData<List<Currency>> getCurrencies() {
        return currencies;
    }

    public LiveData<String> getDateLastUpdate() {
        return dateLastUpdate;
    }

    @SuppressWarnings("unchecked")
    private void insertCurrencies(List<Currency> currencies) {
        new InsertCurrenciesTask().execute(currencies);
    }

    private static class InsertCurrenciesTask extends AsyncTask<List<Currency>, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Currency>... lists) {
            if (lists != null && lists.length > 0) {
                db.currencyDao().insertCurrencies(lists[0]);
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void insertDate(String date) {
        new InsertDateTask().execute(date);
    }

    private static class InsertDateTask extends AsyncTask<String, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(String... lists) {
            if (lists != null && lists.length > 0) {
                db.currencyDao().insertDate(new CurrencyResponse(lists[0]));
            }
            return null;
        }
    }


    private void deleteAllCurrencies() {
        new DeleteAllCurrencies().execute();
    }

    private static class DeleteAllCurrencies extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.currencyDao().deleteAllCurrencies();
            return null;
        }
    }

    private void deleteDate() {
        new DeleteDate().execute();
    }

    private static class DeleteDate extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.currencyDao().deleteDate();
            return null;
        }
    }

    public void loadData() {
        ApiFactory factory = ApiFactory.getInstance();
        ApiService service = factory.getApiService();
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Disposable disposable = service.getCurrencyResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CurrencyResponse>() {
                    @Override
                    public void accept(CurrencyResponse currencyResponse) throws Exception {
                        deleteAllCurrencies();
                        deleteDate();
                        insertCurrencies(currencyResponse.getValute().getCurrenciesList());
                        insertDate(currencyResponse.getDate());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errors.setValue(throwable);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public String getConvertedCurrency(Currency selectedCurrency, String amountDefaultCurrency) {
        return ConverterCurrency.getResult(selectedCurrency, amountDefaultCurrency);
    }

    public String getDateUpdateDB(Context context) {
        if (dateUpdateDB != null && dateUpdateDB != "") {
            return dateUpdateDB;
        } else {
            return context.getString(R.string.time_default);
        }
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void autoUpdate() {
        if (dateLastUpdate.getValue() != null) {
            final long timeToAutoUpdate = new AutoUpdate().isAutoUpdate(dateLastUpdate.getValue());
            if (timeToAutoUpdate >= AutoUpdate.TIME_AUTO_UPDATE) {
                loadData();
            } else {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep((AutoUpdate.TIME_AUTO_UPDATE - timeToAutoUpdate) * 1000);
                            loadData();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }
    }

}
