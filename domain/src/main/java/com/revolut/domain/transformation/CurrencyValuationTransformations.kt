package com.revolut.domain.transformation

import com.revolut.domain.Price
import com.revolut.domain.model.Currency
import com.revolut.domain.model.CurrencyValuation
import java.math.BigDecimal
import java.math.RoundingMode

object CurrencyValuationTransformations {

    fun transformCurrencyValuationForNewCurrency(
        currencyValuation: CurrencyValuation,
        toNewCurrency: Currency
    ): CurrencyValuation? {
        val newCurrencyRate = currencyValuation.rates[toNewCurrency] ?: return null

//        currencyValuation.rates
//            .map {
//                if (it.key != toBaseCurrency) {
//                    currencyValuation[it.key }
//                } else {
//            AbstractMap.SimpleEntry
//                    Map.Entry()
//                }

        val newCurrencyValuation = hashMapOf<Currency, Price>()

        currencyValuation.rates
            .filterKeys { currency -> currency != toNewCurrency }
            .forEach {
                newCurrencyValuation[it.key] =
                    it.value.divide(newCurrencyRate, 4, RoundingMode.HALF_UP)
            }

        newCurrencyValuation[currencyValuation.base] =
            BigDecimal.ONE.divide(newCurrencyRate, 4, RoundingMode.HALF_UP)

        return CurrencyValuation(
            toNewCurrency,
            date = currencyValuation.date,
            rates = newCurrencyValuation
        )
    }
}