package com.revolut.currencycalculator.api.model.response

import com.google.gson.annotations.SerializedName
import com.revolut.domain.Money
import com.revolut.domain.model.Currency
import com.revolut.domain.model.CurrencyValuation

data class CurrencyValuationResponse(
    @SerializedName("base") var base: Currency? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("rates") var rates: Map<Currency, Money>? = null
) {

    fun isValid(): Boolean {
        return base != null
                && rates?.isNotEmpty() ?: false
    }

    fun toDomainModel(): CurrencyValuation {
        return CurrencyValuation(
            base = base!!,
            rates = rates!!,
            date = date
        )
    }

}
