package com.revolut.currencycalculator.api

import com.revolut.currencycalculator.api.model.response.CurrencyValuationResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RevolutApi {

    @GET("latest")
    fun getLatest(@Query("base") currency: String) : Single<CurrencyValuationResponse>
}
