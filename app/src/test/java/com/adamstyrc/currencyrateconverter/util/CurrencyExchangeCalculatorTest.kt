package com.adamstyrc.currencyrateconverter.util

import com.adamstyrc.currencyrateconverter.model.Money
import com.revolut.domain.model.Currency
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal


class CurrencyExchangeCalculatorTest {

    private val currencyExchangeCalculator = CurrencyExchangeCalculator()

    @Test
    fun noResultWhenCurrencyNotProvided() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            emptyMap(),
            Currency.EUR,
            BigDecimal.valueOf(100)
        )

        assertNull(exchangedCurrencyValue)
    }

    @Test
    fun calculate1AmericanDollarsToPLN() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.PLN,
            BigDecimal.ONE
        )

        assertTrue(BigDecimal.valueOf(3.7222)
            .compareTo(exchangedCurrencyValue?.value) == 0)
    }

    @Test
    fun calculate100AmericanDollarsToGBP() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.GBP,
            BigDecimal.valueOf(100)
        )

        assertTrue(BigDecimal.valueOf(77.424)
            .compareTo(exchangedCurrencyValue?.value) == 0)
    }

    @Test
    fun calculate500EurToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            BigDecimal.valueOf(500)
        )

        assertTrue(BigDecimal.valueOf(696.65)
            .compareTo(exchangedCurrencyValue?.value) == 0)
    }

    @Test
    fun calculate1000AndEurAndCentsToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            BigDecimal.valueOf(1000.24)
        )

        assertTrue(BigDecimal.valueOf(1393.634392)
            .compareTo(exchangedCurrencyValue?.value) == 0)
    }

    @Test
    fun calculate0EurToOthers() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            BigDecimal.ZERO
        )

        assertTrue(BigDecimal.valueOf(0.0)
            .compareTo(exchangedCurrencyValue?.value) == 0)
    }

    // Data and mocks for tests
    private val currencyRateDataForUSD = HashMap<String, Money>()
        .apply { put("EUR", BigDecimal.valueOf(0.86195)) }
        .apply { put("GBP", BigDecimal.valueOf(0.77424)) }
        .apply { put("PLN", BigDecimal.valueOf(3.7222)) }


    private val currencyRateDataForEUR = HashMap<String, Money>()
        .apply { put("PLN", BigDecimal.valueOf(3.7222)) }
        .apply { put("AUD", BigDecimal.valueOf(1.3933)) }
}
