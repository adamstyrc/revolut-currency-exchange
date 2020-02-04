package com.adamstyrc.currencyrateconverter.api

import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyValuationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {

    @GET("latest")
    fun getLatest(@Query("base") currency: String) : Single<CurrencyValuationResponse>
}
