package com.oliferov.currencyconverter.screens.currencies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oliferov.currencyconverter.R;
import com.oliferov.currencyconverter.adapters.CurrencyAdapter;
import com.oliferov.currencyconverter.pojo.Currency;

import java.util.ArrayList;
import java.util.List;

public class CurrencyListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCurrency;
    private CurrencyAdapter adapter;
    private CurrencyViewModel viewModel;
    private Currency selectedCurrency;

    private EditText editTextSumInRub;
    private TextView textViewResultSumInCurrency;
    private TextView textViewDate;
    private TextView textViewNameResultSum;
    private static final String BUNDLE_KEY_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        recyclerViewCurrency = findViewById(R.id.recyclerViewCurrency);
        editTextSumInRub = findViewById(R.id.editTextSumInRub);
        textViewResultSumInCurrency = findViewById(R.id.textViewResultSumInCurrency);
        textViewDate = findViewById(R.id.textViewDate);
        textViewNameResultSum = findViewById(R.id.textViewNameResultSum);
        adapter = new CurrencyAdapter();
        adapter.setCurrencies(new ArrayList<>());
        recyclerViewCurrency.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCurrency.setAdapter(adapter);
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_RESULT)) {
            textViewResultSumInCurrency.setText(savedInstanceState.getString(BUNDLE_KEY_RESULT));
        }
        viewModel.getCurrencies().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currencies) {
                adapter.setCurrencies(currencies);
            }
        });
        viewModel.getDateLastUpdate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textViewDate.setText(s);
            }
        });
        adapter.setOnCurrencyClickListener(new CurrencyAdapter.OnCurrencyClickListener() {
            @Override
            public void onCurrencyClick(int position) {
                selectedCurrency = adapter.getCurrencies().get(position);
                textViewNameResultSum.setText(selectedCurrency.getCharCode());
            }
        });
        textViewDate.setText(viewModel.getDateUpdateDB(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewModel.autoUpdate();
        }
    }

    public void onClickUpdateJSON(View view) {
        viewModel.loadData();
        viewModel.getErrors().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                if (throwable != null) {
                    Toast.makeText(CurrencyListActivity.this, getString(R.string.error_download_json), Toast.LENGTH_SHORT).show();
                    viewModel.clearErrors();
                }
            }
        });
        textViewDate.setText(viewModel.getDateUpdateDB(this));
    }

    public void onClickEquals(View view) {
        if (!editTextSumInRub.getText().toString().equals("") &&
                !textViewNameResultSum.getText().toString().equals(getString(R.string.name_currency_default))) {
            textViewResultSumInCurrency.setText(viewModel.getConvertedCurrency(selectedCurrency, editTextSumInRub.getText().toString()));
        } else {
            Toast.makeText(CurrencyListActivity.this, getString(R.string.error_click_equals), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_KEY_RESULT, textViewResultSumInCurrency.getText().toString());
        super.onSaveInstanceState(outState);
    }
}