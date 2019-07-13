package com.adamstyrc.currencyrateconverter.util

import com.adamstyrc.currencyrateconverter.model.Currency
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Test


class CurrencyExchangeCalculatorTest {

    private val currencyExchangeCalculator = CurrencyExchangeCalculator()

    @Test
    fun noResultWhenCurrencyNotProvided() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            emptyMap(),
            Currency.EUR,
            100.0
        )

        assertNull(exchangedCurrencyValue)
    }

    @Test
    fun calculate1AmericanDollarsToPLN() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.PLN,
            1.0
        )

        assertEquals(3.7222, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate100AmericanDollarsToGBP() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.GBP,
            100.0
        )

        assertEquals(77.424, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate500EurToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            500.0
        )

        assertEquals(696.65, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate1000AndEurAndCentsToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            1000.24
        )

        assertEquals(1393.634392, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate0EurToOthers() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            0.0
        )

        assertEquals(0.0, exchangedCurrencyValue?.value)
    }

    // Data and mocks for tests
    private val currencyRateDataForUSD = HashMap<String, Double>()
        .apply { put("EUR", 0.86195) }
        .apply { put("GBP", 0.77424) }
        .apply { put("PLN", 3.7222) }


    private val currencyRateDataForEUR = HashMap<String, Double>()
        .apply { put("PLN", 3.7222) }
        .apply { put("AUD", 1.3933) }
}
