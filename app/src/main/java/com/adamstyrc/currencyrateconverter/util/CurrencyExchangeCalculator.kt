package com.adamstyrc.currencyrateconverter.util

import com.adamstyrc.currencyrateconverter.model.EstimatedCurrencyExchange
import com.adamstyrc.currencyrateconverter.model.Money
import com.revolut.domain.model.Currency

class CurrencyExchangeCalculator {

    fun calculate(
        currencyRatesData: Map<String, Money>,
        wantedCurrency: Currency,
        exchangedValue: Money
    ) : EstimatedCurrencyExchange? {
        val exchangeRate = currencyRatesData[wantedCurrency.name]

        if (exchangeRate != null) {
            val calculatedValue = exchangeRate * exchangedValue
            return EstimatedCurrencyExchange(wantedCurrency, calculatedValue)
        } else {
            return null
        }
    }
}
