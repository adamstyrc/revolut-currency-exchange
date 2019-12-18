package com.adamstyrc.currencyrateconverter.api.model.response

import com.adamstyrc.currencyrateconverter.model.Money
import com.google.gson.annotations.SerializedName

class CurrencyRateResponse {

    @SerializedName("base")
    var base: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("rates")
    var rates: Map<String, Money>? = null

}
