package com.adamstyrc.currencyrateconverter.api.model.response

import com.google.gson.annotations.SerializedName

class CurrencyRateResponse {

    var base: String? = null
    var date: String? = null

    @SerializedName("rates")
    var rates: Map<String, Float>? = null

}
