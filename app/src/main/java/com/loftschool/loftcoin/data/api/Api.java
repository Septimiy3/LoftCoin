package com.loftschool.loftcoin.data.api;

import com.loftschool.loftcoin.data.api.model.RateResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Api {

    String CONVERT = "USD,EUR,RUB";

    @GET("cryptocurrency/listings/latest")
    @Headers("X-CMC_PRO_API_KEY: ed39593b-6dcc-4e0e-9e5f-9069bf820132")
    Observable<RateResponse> rates(@Query("convert") String convert);


}
