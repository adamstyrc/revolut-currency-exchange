package com.revolut.domain.calculator

import com.revolut.domain.Money
import com.revolut.domain.model.Currency
import com.revolut.domain.model.EstimatedCurrencyExchange

class CurrencyExchangeCalculator {

    fun calculate(
        currencyRatesData: Map<Currency, Money>,
        wantedCurrency: Currency,
        exchangedValue: Money
    ) : EstimatedCurrencyExchange? {
        val exchangeRate = currencyRatesData[wantedCurrency]

        if (exchangeRate != null) {
            val calculatedValue = exchangeRate * exchangedValue
            return EstimatedCurrencyExchange(wantedCurrency, calculatedValue)
        } else {
            return null
        }
    }
}