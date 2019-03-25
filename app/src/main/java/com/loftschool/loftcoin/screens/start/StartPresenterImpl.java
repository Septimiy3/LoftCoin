package com.loftschool.loftcoin.screens.start;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.model.RateResponse;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.utils.Fiat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class StartPresenterImpl implements StartPresenter {

    private Prefs prefs;

    private Api api;

    @Nullable
    private StartView view;

    public StartPresenterImpl(Prefs prefs, Api api) {
        this.prefs = prefs;
        this.api = api;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadRates() {

        Fiat fiat = prefs.getFiatCurrency();

        Call<RateResponse> call = api.rates(Api.CONVERT);

        call.enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {
                if (view != null) {
                    view.navigateToMainScreen();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateResponse> call, Throwable t) {
                Timber.e(t);
            }
        });
    }
}
