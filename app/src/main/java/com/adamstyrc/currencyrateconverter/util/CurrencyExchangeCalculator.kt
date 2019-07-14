package com.adamstyrc.currencyrateconverter.util

import com.adamstyrc.currencyrateconverter.model.EstimatedCurrency
import com.adamstyrc.currencyrateconverter.model.Currency

class CurrencyExchangeCalculator {

    fun calculate(
        currencyRatesData: Map<String, Float>,
        wantedCurrency: Currency,
        exchangedValue: Float
    ) : EstimatedCurrency? {
        val exchangeRate = currencyRatesData[wantedCurrency.name]

        if (exchangeRate != null) {
            val calculatedValue = exchangeRate * exchangedValue
            return EstimatedCurrency(wantedCurrency, calculatedValue)
        } else {
            return null
        }
    }
}
