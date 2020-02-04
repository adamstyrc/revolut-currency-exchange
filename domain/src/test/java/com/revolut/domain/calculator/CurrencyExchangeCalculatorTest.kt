package com.revolut.domain.calculator

import com.google.common.truth.Truth.assertThat
import com.revolut.domain.Money
import com.revolut.domain.model.Currency
import org.junit.Test
import java.math.BigDecimal


class CurrencyExchangeCalculatorTest {

    private val currencyExchangeCalculator = CurrencyExchangeCalculator()

    // Data and mocks for tests
    private val currencyRateDataForUSD = hashMapOf(
        Currency.EUR to BigDecimal.valueOf(0.86195),
        Currency.GBP to BigDecimal.valueOf(0.77424),
        Currency.PLN to BigDecimal.valueOf(3.7222)
    )

    private val currencyRateDataForEUR = hashMapOf(
        Currency.PLN to BigDecimal.valueOf(3.7222),
        Currency.AUD to BigDecimal.valueOf(1.3933)
    )

    @Test
    fun noResultWhenCurrencyNotProvided() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            emptyMap(),
            Currency.EUR,
            BigDecimal.valueOf(100)
        )

        assertThat(exchangedCurrencyValue)
            .isNull()
    }

    @Test
    fun calculate1AmericanDollarsToPLN() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.PLN,
            BigDecimal.ONE
        )

        assertThat(exchangedCurrencyValue?.value)
            .isEqualToIgnoringScale(BigDecimal.valueOf(3.7222))
    }

    @Test
    fun calculate100AmericanDollarsToGBP() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForUSD,
            Currency.GBP,
            BigDecimal.valueOf(100)
        )

        assertThat(exchangedCurrencyValue?.value)
            .isEqualToIgnoringScale(BigDecimal.valueOf(77.424))
    }

    @Test
    fun calculate500EurToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            BigDecimal.valueOf(500)
        )

        assertThat(exchangedCurrencyValue?.value)
            .isEqualToIgnoringScale(BigDecimal.valueOf(696.65))
    }

    @Test
    fun calculate1000AndEurAndCentsToAUD() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            BigDecimal.valueOf(1000.24)
        )

        assertThat(exchangedCurrencyValue?.value)
            .isEqualToIgnoringScale(BigDecimal.valueOf(1393.634392))
    }

    @Test
    fun calculate0EurToOthers() {
        val exchangedCurrencyValue = currencyExchangeCalculator.calculate(
            currencyRateDataForEUR,
            Currency.AUD,
            BigDecimal.ZERO
        )

        assertThat(exchangedCurrencyValue?.value)
            .isEqualToIgnoringScale(BigDecimal.valueOf(0.0))
    }
}
