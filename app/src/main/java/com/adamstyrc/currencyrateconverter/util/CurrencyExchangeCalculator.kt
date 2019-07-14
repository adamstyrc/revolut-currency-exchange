package com.adamstyrc.currencyrateconverter.util

import com.adamstyrc.currencyrateconverter.model.CalculatedCurrency
import com.adamstyrc.currencyrateconverter.model.Currency

class CurrencyExchangeCalculator {

    fun calculate(
        currencyRatesData: Map<String, Float>,
        wantedCurrency: Currency,
        exchangedValue: Float
    ) : CalculatedCurrency? {
        val exchangeRate = currencyRatesData[wantedCurrency.name]

        if (exchangeRate != null) {
            val calculatedValue = exchangeRate * exchangedValue
            return CalculatedCurrency(wantedCurrency, calculatedValue)
        } else {
            return null
        }
    }
}
