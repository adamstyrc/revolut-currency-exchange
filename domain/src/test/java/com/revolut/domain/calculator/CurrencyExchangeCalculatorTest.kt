package com.revolut.domain.calculator

import com.google.common.truth.Truth.assertThat
import com.revolut.domain.model.Currency
import com.revolut.testpack.CurrencyValuationTestData
import org.junit.Test
import java.math.BigDecimal


class CurrencyExchangeCalculatorTest {

    private val currencyExchangeCalculator = CurrencyExchangeCalculator()
    private val currencyValuationTestData = CurrencyValuationTestData()

    @Test
    fun noResultWhenCurrencyNotProvided() {
        val calculatedCurrencyPrice = currencyExchangeCalculator.calculateBoughtCurrencyValue(
            Currency.USD,
            BigDecimal.valueOf(100),
            Currency.EUR,
            currencyValuationTestData.ratesForUSDempty
        )

        assertThat(calculatedCurrencyPrice)
            .isNull()
    }

    @Test
    fun calculate1AmericanDollarsToPLN() {
        val calculatedCurrencyPrice = currencyExchangeCalculator.calculateBoughtCurrencyValue(
            Currency.USD,
            BigDecimal.valueOf(1),
            Currency.PLN,
            currencyValuationTestData.ratesForUSD1
        )

        assertThat(calculatedCurrencyPrice)
            .isEqualToIgnoringScale(BigDecimal.valueOf(3.7222))
    }

    @Test
    fun calculate100AmericanDollarsToGBP() {
        val calculatedCurrencyPrice = currencyExchangeCalculator.calculateBoughtCurrencyValue(
            Currency.USD,
            BigDecimal.valueOf(100),
            Currency.GBP,
            currencyValuationTestData.ratesForUSD1
        )

        assertThat(calculatedCurrencyPrice)
            .isEqualToIgnoringScale(BigDecimal.valueOf(77.424))
    }

    @Test
    fun calculate500EurToAUD() {
        val calculatedCurrencyPrice = currencyExchangeCalculator.calculateBoughtCurrencyValue(
            Currency.EUR,
            BigDecimal.valueOf(500),
            Currency.AUD,
            currencyValuationTestData.ratesForEUR2
        )

        assertThat(calculatedCurrencyPrice)
            .isEqualToIgnoringScale(BigDecimal.valueOf(696.65))
    }

    @Test
    fun calculate1000AndEurAndCentsToAUD() {
        val calculatedCurrencyPrice = currencyExchangeCalculator.calculateBoughtCurrencyValue(
            Currency.EUR,
            BigDecimal.valueOf(1000.24),
            Currency.AUD,
            currencyValuationTestData.ratesForEUR2
        )

        assertThat(calculatedCurrencyPrice)
            .isEqualToIgnoringScale(BigDecimal.valueOf(1393.634392))
    }

    @Test
    fun calculate0EurToOthers() {
        val calculatedCurrencyPrice = currencyExchangeCalculator.calculateBoughtCurrencyValue(
            Currency.EUR,
            BigDecimal.valueOf(0),
            Currency.AUD,
            currencyValuationTestData.ratesForEUR2
        )

        assertThat(calculatedCurrencyPrice)
            .isEqualToIgnoringScale(BigDecimal.valueOf(0.0))
    }
}
