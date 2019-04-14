package com.loftschool.loftcoin.screens.converter;


import android.os.Bundle;

import com.loftschool.loftcoin.data.db.model.CoinEntity;

import io.reactivex.Observable;

public interface ConverterViewModel {

    Observable<String> sourceCurrency();

    Observable<String> destinationCurrency();

    Observable<String> destinationAmount();

    Observable<Object> selectSourceCurrency();

    Observable<Object> selectDestinationCurrency();


    void onSourceAmountChange(String amount);

    void onSourceCurrencySelected(CoinEntity coin);

    void onDestinationCurrencySelected(CoinEntity coin);

    void onSourceCurrencyClick();

    void onDestinationCurrencyClick();

    void saveState(Bundle state);

    void onDetach();
}
