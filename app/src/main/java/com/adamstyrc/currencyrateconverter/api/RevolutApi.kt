package com.adamstyrc.currencyrateconverter.api

import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {

    @GET("latest")
    fun get(@Query("base") currency: String) : Observable<CurrencyRateResponse>


}
