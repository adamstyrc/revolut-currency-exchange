package com.adamstyrc.currencyrateconverter.api

import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {

    @GET("latest")
    fun get(@Query("base") currency: String) : Single<CurrencyRateResponse>


}
