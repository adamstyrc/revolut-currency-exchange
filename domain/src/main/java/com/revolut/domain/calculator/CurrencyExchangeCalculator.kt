package com.revolut.domain.calculator

import com.revolut.domain.Price
import com.revolut.domain.model.Currency
import com.revolut.domain.model.CurrencyValuation

class CurrencyExchangeCalculator {

    fun calculateBoughtCurrencyValue(
        soldCurrency: Currency,
        soldCurrencyValue: Price,
        boughtCurrency: Currency,
        currencyValuation: CurrencyValuation
    ): Price? {
        if (soldCurrency == boughtCurrency) {
            return soldCurrencyValue
        }

        if (soldCurrency == currencyValuation.base) {
            currencyValuation.rates[boughtCurrency]?.let { currencyExchangeRate ->
                return calculateBoughtCurrencyValue(currencyExchangeRate, soldCurrencyValue)
            }
        }

        return null
    }

    private fun calculateBoughtCurrencyValue(
        soldCurrencyValue: Price,
        currencyRate: Price
    ): Price {
        return currencyRate.multiply(soldCurrencyValue)
    }

//    fun calculate(
//        currencyRatesData: Map<Currency, Price>,
//        wantedCurrency: Currency,
//        exchangedBaseCurrencyValue: Price
//    ) : EstimatedCurrencyExchange? {
//        val exchangeRate = currencyRatesData[wantedCurrency]
//
//        if (exchangeRate != null) {
//            val calculatedValue = exchangeRate * exchangedBaseCurrencyValue
//            return EstimatedCurrencyExchange(wantedCurrency, calculatedValue)
//        } else {
//            return null
//        }
//    }
}