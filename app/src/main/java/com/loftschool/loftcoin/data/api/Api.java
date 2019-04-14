package com.loftschool.loftcoin.data.api;

import com.loftschool.loftcoin.data.api.model.RateResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Api {

    String CONVERT = "USD";

    @GET("cryptocurrency/listings/latest")
    @Headers("X-CMC_PRO_API_KEY: 9f823db0-6f62-4827-b3a3-bf50a0e31f7f")
    Observable<RateResponse> rates(@Query("convert") String convert);


}
