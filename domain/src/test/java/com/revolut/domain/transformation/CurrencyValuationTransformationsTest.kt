package com.revolut.domain.transformation

import com.google.common.truth.Truth.assertThat
import com.revolut.domain.model.Currency
import com.revolut.testpack.CurrencyValuationTestData
import org.junit.Test
import java.math.BigDecimal

class CurrencyValuationTransformationsTest {

    private val currencyValuationTestData = CurrencyValuationTestData()

    @Test
    fun fromUSDtoEUR_baseProperlySet() {
        val currencyValuationForAnotherCurrency =
            CurrencyValuationTransformations.transformCurrencyValuationForNewCurrency(
                currencyValuationTestData.ratesForUSD1,
                Currency.EUR
            )

        assertThat(currencyValuationForAnotherCurrency!!.base)
            .isEqualTo(Currency.EUR)
    }

    @Test
    fun fromUSDtoEUR_ratesCorrect() {
        val currencyValuationForAnotherCurrency =
            CurrencyValuationTransformations.transformCurrencyValuationForNewCurrency(
                currencyValuationTestData.ratesForUSD1,
                Currency.EUR
            )

        val currencyValuationForEUR = currencyValuationForAnotherCurrency!!
        assertThat(currencyValuationForEUR.rates[Currency.USD])
            .isEqualToIgnoringScale(BigDecimal.valueOf(1.1602))

        assertThat(currencyValuationForAnotherCurrency.rates[Currency.PLN])
            .isEqualToIgnoringScale(BigDecimal.valueOf(4.3183))
    }

    @Test
    fun fromEURtoUSD_ratesCorrect() {
        val currencyValuationForAnotherCurrency =
            CurrencyValuationTransformations.transformCurrencyValuationForNewCurrency(
                currencyValuationTestData.ratesForEUR1,
                Currency.USD
            )

        val currencyValuationForEUR = currencyValuationForAnotherCurrency!!
        assertThat(currencyValuationForEUR.rates[Currency.EUR])
            .isEqualToIgnoringScale(BigDecimal.valueOf(0.8620))

        assertThat(currencyValuationForAnotherCurrency.rates[Currency.PLN])
            .isEqualToIgnoringScale(BigDecimal.valueOf(3.7222))
    }
}