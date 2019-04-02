package com.loftschool.loftcoin.screens.rate;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.model.RateResponse;
import com.loftschool.loftcoin.data.prefs.Prefs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RatePresenterImpl implements RatePresenter {

    private Prefs prefs;
    private Api api;


    @Nullable
    private RateView view;

    public RatePresenterImpl(Prefs prefs, Api api) {
        this.prefs = prefs;
        this.api = api;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getRate() {

        Call<RateResponse> call = api.rates(Api.CONVERT);

        call.enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {

                RateResponse body = response.body();

                if (view != null && body != null) {
                    view.setCoins(body.data);
                    view.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateResponse> call, Throwable t) {
                Timber.e(t);

                if (view != null) {
                    view.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getRate();
    }
}
