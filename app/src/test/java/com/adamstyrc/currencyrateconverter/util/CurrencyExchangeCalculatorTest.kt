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
            100f
        )

        assertNull(exchangedCurrencyValue)
    }

    @Test
    fun calculate1AmericanDollarsToPLN() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.PLN,
            1f
        )

        assertEquals(3.7222f, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate100AmericanDollarsToGBP() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.GBP,
            100f
        )

        assertEquals(77.424f, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate500EurToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            500f
        )

        assertEquals(696.65f, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate1000AndEurAndCentsToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            1000.24f
        )

        assertEquals(1393.634392f, exchangedCurrencyValue?.value)
    }

    @Test
    fun calculate0EurToOthers() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            0.0f
        )

        assertEquals(0.0f, exchangedCurrencyValue?.value)
    }

    // Data and mocks for tests
    private val currencyRateDataForUSD = HashMap<String, Float>()
        .apply { put("EUR", 0.86195f) }
        .apply { put("GBP", 0.77424f) }
        .apply { put("PLN", 3.7222f) }


    private val currencyRateDataForEUR = HashMap<String, Float>()
        .apply { put("PLN", 3.7222f) }
        .apply { put("AUD", 1.3933f) }
}
