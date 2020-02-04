package com.adamstyrc.currencyrateconverter.api.model.response

import com.google.gson.annotations.SerializedName
import com.revolut.domain.Money

class CurrencyRateResponse {

    @SerializedName("base")
    var base: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("rates")
    var rates: Map<String, Money>? = null

}
