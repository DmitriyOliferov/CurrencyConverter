package com.oliferov.currencyconverter.converter;

import com.oliferov.currencyconverter.pojo.Currency;

import java.text.DecimalFormat;

public interface ConverterCurrency {
    static String getResult(Currency selectedCurrency, String amountDefaultCurrency) {
        double resultDouble = Double.parseDouble(amountDefaultCurrency) /
                (selectedCurrency.getValue() / selectedCurrency.getNominal());
        String resultString = new DecimalFormat("#0.00").format(resultDouble);
        return resultString;
    }
}
