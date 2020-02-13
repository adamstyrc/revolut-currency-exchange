package com.revolut.domain

import com.revolut.domain.model.Currency
import com.revolut.domain.model.CurrencyValuation
import java.math.BigDecimal

class CurrencyValuationTestData {

    val ratesFromUSDempty= CurrencyValuation(
        base = Currency.USD,
        rates = emptyMap()
    )

    val ratesFromUSD1 = CurrencyValuation(
        base = Currency.USD,
        rates = hashMapOf(
            Currency.EUR to BigDecimal.valueOf(0.86195),
            Currency.GBP to BigDecimal.valueOf(0.77424),
            Currency.PLN to BigDecimal.valueOf(3.7222),
            Currency.AUD to BigDecimal.valueOf(1.3906)
        )
    )

    val ratesFromEUR1 = CurrencyValuation(
        base = Currency.EUR,
        rates = hashMapOf(
            Currency.USD to BigDecimal.valueOf(1.16016),
            Currency.AUD to BigDecimal.valueOf(1.61331),
            Currency.PLN to BigDecimal.valueOf(4.31834)
        )
    )

    val ratesFromEUR2 = CurrencyValuation(
        base = Currency.EUR,
        rates = hashMapOf(
            Currency.USD to BigDecimal.valueOf(1.16016),
            Currency.AUD to BigDecimal.valueOf(1.3933),
            Currency.PLN to BigDecimal.valueOf(3.7222)
        )
    )
}